package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.BasicCommand
import foxparade.mongo.EventRepository
import foxparade.mongo.TicketRepository
import foxparade.mongo.model.Event
import foxparade.util.OptionExtractor
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MyTicketsCommand(
    val ticketRepository: TicketRepository,
    val eventRepository: EventRepository,
) : BasicCommand() {
    override fun getName(): String {
        return "my_tickets"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val eventId: String = OptionExtractor.extractStringOption(event, "event").lowercase()

        val eventExists: Mono<Boolean> =
            eventRepository.existsByIdAndIsActiveIs(eventId)

        return eventExists
            .flatMap { getEvent(it, event, eventId) }
            .flatMap { getMyTicketFollowup(eventId, event, it) }
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
            eventRepository.findById(eventId)
        }
    }

    private fun getMyTicketFollowup(
        eventId: String,
        event: ChatInputInteractionEvent,
        ticketEvent: Event
    ): Mono<Any> {
        return ticketRepository.existsByEventIdAndUserId(eventId, event.interaction.user.id.asLong())
            .flatMap { ticketExists ->
                if (ticketExists) {
                    ticketRepository.findAllByEventIdAndUserId(eventId, event.interaction.user.id.asLong())
                        .map { ticket -> ticket.number }
                        .collectList()
                        .flatMap {
                            it.joinToString(",")
                            event.createFollowup(
                                "Your tickets for event ${ticketEvent.id} are: ${it.joinToString(",")}"
                            )
                        }
                } else {
                    event.createFollowup("You have no tickets for event ${ticketEvent.id}.")
                }
            }
    }

}