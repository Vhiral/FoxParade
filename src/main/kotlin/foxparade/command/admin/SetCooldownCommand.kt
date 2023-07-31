package foxparade.command.admin

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.EventRegisterCommand
import foxparade.command.logic.CommandRegistrar
import foxparade.mongo.EventRepository
import foxparade.util.OptionExtractor
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SetCooldownCommand(
    private val eventRepository: EventRepository,
    commandRegistrar: CommandRegistrar
) :
    EventRegisterCommand(commandRegistrar) {
    override fun getName(): String {
        return "set_cooldown"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val id: String = OptionExtractor.extractStringOption(event, "event")
        val cooldownMs: Long = OptionExtractor.extractLongOption(event, "cooldown")

        return eventRepository.existsById(id)
            .flatMap {
                if (!it) {
                    event.createFollowup(
                        "An event with the name $id does not exists!"
                    )
                } else {
                    eventRepository.findById(id)
                        .flatMap { event ->
                            event.cooldown = cooldownMs
                            eventRepository.save(event)
                        }
                        .then(event.createFollowup("Updated $id!"))
                }
            }
    }
}