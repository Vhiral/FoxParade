package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.InteractionFollowupCreateMono
import foxparade.command.BasicCommand
import foxparade.command.logic.line.TicketRandomLine
import foxparade.mongo.CooldownRepository
import foxparade.mongo.EventRepository
import foxparade.mongo.TicketRepository
import foxparade.mongo.model.Cooldown
import foxparade.mongo.model.Event
import foxparade.mongo.model.Ticket
import foxparade.util.OptionExtractor
import foxparade.util.TimeUtil
import foxparade.util.TimeUtil.Companion.getCooldownString
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Component
class TicketCommand(
    private val eventRepository: EventRepository,
    private val cooldownRepository: CooldownRepository,
    private val ticketRepository: TicketRepository,
    private val ticketRandomLine: TicketRandomLine,
) : BasicCommand() {

    override fun getName(): String {
        return "ticket"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val eventId: String = OptionExtractor.extractStringOption(event, "event").lowercase()

        val eventExists: Mono<Boolean> =
            eventRepository.existsByIdAndStartTimeIsLessThanEqualAndIsActiveIs(
                eventId,
                LocalDateTime.now(ZoneId.of("UTC"))
            )

        return eventExists
            .flatMap { getEvent(it, event, eventId) }
            .flatMap { checkCooldownAndHandlePull(event, it) }
    }

    private fun getEvent(
        it: Boolean,
        event: ChatInputInteractionEvent,
        eventId: String
    ): Mono<Event> {
        return if (!it) {
            event.createFollowup(
                "The event $eventId seems to either not exist or has not started yet!"
            )
            Mono.empty()
        } else {
            eventRepository.getByIdAndStartTimeIsLessThanEqual(eventId, LocalDateTime.now(ZoneId.of("UTC")))
        }
    }

    private fun checkCooldownAndHandlePull(
        event: ChatInputInteractionEvent,
        it: Event
    ): Mono<Any> {
        val now: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))

        val cooldownExists: Mono<Boolean> = cooldownRepository.existsById(
            Cooldown.CooldownId(
                event.interaction.user.id.asLong(),
                it.id
            )
        )

        return cooldownExists.flatMap { exists ->
            if (exists) {
                val cooldown: Mono<Cooldown> = cooldownRepository.findById(
                    Cooldown.CooldownId(
                        event.interaction.user.id.asLong(),
                        it.id
                    )
                )

                cooldown.flatMap { cd ->
                    if (TimeUtil.isOnCooldown(cd.lastPull, now, it.cooldown)) {
                        getFollowupForUserOnCooldown(event, cd.lastPull, now, it)
                    } else {
                        pullTicket(it, event, now)
                    }
                }
            } else {
                pullTicket(it, event, now)
            }
        }
    }

    private fun getFollowupForUserOnCooldown(
        interactionEvent: ChatInputInteractionEvent,
        lastPull: LocalDateTime,
        now: LocalDateTime,
        event: Event
    ): InteractionFollowupCreateMono {
        return interactionEvent.createFollowup(
            "${interactionEvent.interaction.user.username} you are currently on cooldown and can draw another ticket in " +
                    getCooldownString(lastPull, now, event.cooldown)
        )
    }

    private fun pullTicket(
        event: Event,
        interactionEvent: ChatInputInteractionEvent,
        now: LocalDateTime
    ): Mono<Any> {
        return getNumber(event)
            .flatMap { number ->
                Mono.zip(
                    ticketRepository.save(Ticket(event.id, interactionEvent.interaction.user.id.asLong(), number)),
                    cooldownRepository.save(
                        Cooldown(
                            Cooldown.CooldownId(
                                interactionEvent.interaction.user.id.asLong(),
                                event.id
                            ), now
                        )
                    )
                ).flatMap<Any?> {
                    interactionEvent.createFollowup(
                        ticketRandomLine.getRandomLine()
                            .format(interactionEvent.interaction.user.username, number.toString())
                    )
                }
            }
    }

    private fun getNumber(event: Event): Mono<Int> {
        return ticketRepository.existsByEventId(event.id).flatMap {
            if (!it) {
                Mono.just(1)
            } else {
                ticketRepository.findByEventId(event.id).flatMap { number ->
                    Mono.just(number.number.plus(1))
                }
            }
        }
    }

}
