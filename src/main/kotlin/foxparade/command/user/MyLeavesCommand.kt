package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.BasicCommand
import foxparade.mongo.LeafRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MyLeavesCommand(
    private val leafRepository: LeafRepository
) : BasicCommand() {

    override fun getName(): String {
        return "my_leaves"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        return leafRepository.existsById(event.interaction.user.id.asLong())
            .flatMap<Any?> {
                if (it) {
                    leafRepository.findById(event.interaction.user.id.asLong())
                        .flatMap { leaf -> Mono.just(leaf.leaves) }
                } else {
                    Mono.just(0)
                }
            }.flatMap {
                event.createFollowup(
                    "${event.interaction.user.username} you currently have $it leaves."
                )
            }
    }
}