package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.BasicCommand
import foxparade.command.logic.loot.ToyMaker
import foxparade.command.logic.loot.toy.Toy
import foxparade.mongo.LeafRepository
import foxparade.mongo.ToyRepository
import foxparade.util.OptionExtractor
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class BuyToyCommand(
    private val leafRepository: LeafRepository,
    private val toyMaker: ToyMaker,
    private val toyRepository: ToyRepository
) : BasicCommand() {

    override fun getName(): String {
        return "buy_toy"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val spentLeaves: Long = OptionExtractor.extractLongOption(event, "leaves")

        return leafRepository.existsById(event.interaction.user.id.asLong())
            .flatMap {
                if (!it) {
                    event.createFollowup(
                        "*Pancake refuses to give you a toy. You don't have enough leaves.*"
                    )
                    Mono.empty()
                } else {
                    leafRepository.findById(event.interaction.user.id.asLong())
                }
            }.flatMap { leaf ->
                if (leaf.leaves >= spentLeaves) {
                    val pulledToy: Toy = toyMaker.rollForLoot(spentLeaves)
                    leafRepository.decrementUserLeaves(event.interaction.user.id.asLong(), spentLeaves)
                        .flatMap {
                            toyRepository.insert(
                                foxparade.mongo.model.Toy(
                                    event.interaction.user.id.asLong(),
                                    pulledToy.getFullToyName(),
                                    pulledToy.getToyName(),
                                    pulledToy.getColor(),
                                    pulledToy.getModifier(),
                                    pulledToy.getRarity(),
                                    LocalDateTime.now(ZoneId.of("UTC"))
                                )
                            ).flatMap {
                                event.createFollowup(
                                    "*${event.interaction.user.username} gives Pancake $spentLeaves leaves.*\n" +
                                            "*Pancake reaches into his toy box and gives ${event.interaction.user.username} a* " +
                                            "${pulledToy.getFullToyName()} (${pulledToy.getRarity().value})!"
                                )
                            }
                        }
                } else {
                    event.createFollowup(
                        "*Pancake refuses to give you a toy. You don't have enough leaves.*"
                    )
                }
            }

    }
}