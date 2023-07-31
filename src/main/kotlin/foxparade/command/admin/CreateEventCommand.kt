package foxparade.command.admin

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.EventRegisterCommand
import foxparade.command.logic.CommandRegistrar
import foxparade.mongo.EventRepository
import foxparade.mongo.dto.Event
import foxparade.util.OptionExtractor
import foxparade.util.TimeUtil
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateEventCommand(
    private val eventRepository: EventRepository,
    commandRegistrar: CommandRegistrar
) : EventRegisterCommand(commandRegistrar) {
    override fun getName(): String {
        return "create_event"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val id: String = OptionExtractor.extractStringOption(event, "event")
        val startTime: String = OptionExtractor.extractStringOption(event, "start_time")

        return eventRepository.existsById(id)
            .flatMap {
                if (it) {
                    event.createFollowup(
                        "An event with the name $id already exists! Please consider using a different " +
                                "name or deleting the old event."
                    )
                } else {
                    eventRepository.save(Event(id, TimeUtil.convertJstStringToUtcZonedDateTime(startTime)))
                        .then(event.createFollowup("Created new event $id!"))
                }
            }
    }
}