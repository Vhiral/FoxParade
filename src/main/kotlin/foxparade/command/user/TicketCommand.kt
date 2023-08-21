package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
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
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
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

        return eventRepository.getByIdAndStartTimeIsLessThanEqual(eventId, LocalDateTime.now(ZoneId.of("UTC")))
            .flatMap {
                eventFollowup(it, event, eventId)
            }
    }

    private fun eventFollowup(it: Event?, interactionEvent: ChatInputInteractionEvent, eventId: String): Mono<Any> {
        return mono {
            it?.let {
                cooldownRepository.findByUserIdAndEventId(interactionEvent.interaction.user.id.asString(), it.id)
                    .flatMap { cooldown ->
                        cooldownFollowup(cooldown, it, interactionEvent)
                    }
            } ?: interactionEvent.createFollowup(
                "The event $eventId seems to either not exist or has not started yet!"
            )
        }
    }

    private fun cooldownFollowup(
        cooldown: Cooldown?,
        event: Event,
        interactionEvent: ChatInputInteractionEvent,
    ): Mono<Any> {
        val now: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
        val lastPull: LocalDateTime = cooldown?.lastPull
            ?: now.minus(event.cooldown, ChronoUnit.MILLIS)

        return if (TimeUtil.isOnCooldown(lastPull, now, event.cooldown)) {
            getFollowupForUserOnCooldown(interactionEvent, lastPull, now, event)
        } else {
            pullTicket(event, interactionEvent, now)
        }
    }

    private fun getFollowupForUserOnCooldown(
        interactionEvent: ChatInputInteractionEvent,
        lastPull: LocalDateTime,
        now: LocalDateTime,
        event: Event
    ): Mono<Any> {
        return mono {
            interactionEvent.createFollowup(
                "${interactionEvent.interaction.user.username} you are currently on cooldown and can draw another ticket in " +
                        getCooldownString(lastPull, now, event.cooldown)
            )
        }
    }

    private fun getCooldownString(lastPull: LocalDateTime, now: LocalDateTime, cooldown: Long): String {
        val cooldownDifferenceMs: Long = cooldown - Duration.between(lastPull, now).toMillis()
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(cooldownDifferenceMs)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(cooldownDifferenceMs) - TimeUnit.MINUTES.toSeconds(minutes)

        return if (minutes == 0L) "$seconds seconds." else "$minutes minutes $seconds seconds."
    }

    private fun pullTicket(
        event: Event,
        interactionEvent: ChatInputInteractionEvent,
        now: LocalDateTime
    ): Mono<Any> {
        return getNumber(event)
            .flatMap {
                interactionEvent.createFollowup(
                    ticketRandomLine.getRandomLine()
                        .format(interactionEvent.interaction.user.username, it.toString())
                )
                saveTicketAndCooldown(
                    now,
                    event.id,
                    interactionEvent.interaction.user.id.toString(),
                    it
                )
            }
    }

    private fun getNumber(event: Event): Mono<Int> {
        return ticketRepository.findByEventId(event.id).map {
            (it?.number?.plus(1)) ?: 0
        }
    }

    private fun saveTicketAndCooldown(
        now: LocalDateTime,
        eventId: String,
        userId: String,
        number: Int
    ): Mono<Void> {
        return Mono.zip(
            ticketRepository.save(Ticket(eventId, userId, number)),
            cooldownRepository.save(Cooldown(userId, eventId, now))
        ).then()
    }
}
