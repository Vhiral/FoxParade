package foxparade.command.admin

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.EventRegisterCommand
import foxparade.command.logic.CommandRegistrar
import foxparade.mongo.EventRepository
import foxparade.util.OptionExtractor
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ActivateEventCommand(
    private val eventRepository: EventRepository,
    commandRegistrar: CommandRegistrar
) : EventRegisterCommand(commandRegistrar) {

    override fun getName(): String {
        return "activate_event"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val id: String = OptionExtractor.extractStringOption(event, "event")
        val isActive: Boolean = OptionExtractor.extractBooleanOption(event, "visibility")

        return eventRepository.existsById(id)
            .flatMap {
                if (!it) {
                    event.createFollowup(
                        "An event with the name $id does not exist! Please check that you are using the correct" +
                                "event name!"
                    )
                } else {
                    eventRepository.findById(id)
                        .flatMap { event ->
                            event.isActive = isActive
                            eventRepository.save(event)
                        }
                        .then(event.createFollowup("Updated $id!"))
                }
            }
    }
}