package foxparade.command.admin

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.EventRegisterCommand
import foxparade.command.logic.CommandRegistrar
import foxparade.mongo.OrderRepository
import foxparade.util.OptionExtractor
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateOrderCommand(
    private val orderRepository: OrderRepository,
    commandRegistrar: CommandRegistrar
) : EventRegisterCommand(commandRegistrar) {

    override fun getName(): String {
        return "update_order"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val id: String = OptionExtractor.extractStringOption(event, "order")
        val description: String? = OptionExtractor.extractStringOptionNullable(event, "description")
        val isActive: Boolean? = OptionExtractor.extractBooleanNullable(event, "visibility")

        return orderRepository.existsById(id)
            .flatMap {
                if (!it) {
                    event.createFollowup(
                        "An order with the name $id does not exist! Please check that you are using the correct" +
                                "order name!"
                    )
                } else {
                    orderRepository.findById(id)
                        .flatMap { order ->
                            order.isActive = isActive ?: order.isActive
                            order.description = description ?: order.description
                            orderRepository.save(order)
                        }
                        .then(event.createFollowup("Updated $id!"))
                }
            }
    }
}