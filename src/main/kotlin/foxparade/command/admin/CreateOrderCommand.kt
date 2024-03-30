package foxparade.command.admin

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.EventRegisterCommand
import foxparade.command.logic.CommandRegistrar
import foxparade.mongo.EventRepository
import foxparade.mongo.OrderRepository
import foxparade.mongo.model.Event
import foxparade.mongo.model.Order
import foxparade.util.OptionExtractor
import foxparade.util.TimeUtil
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateOrderCommand(
    private val orderRepository: OrderRepository,
    commandRegistrar: CommandRegistrar
) : EventRegisterCommand(commandRegistrar) {

    override fun getName(): String {
        return "create_order"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val id: String = OptionExtractor.extractStringOption(event, "order") ?: ""
        val description: String = OptionExtractor.extractStringOption(event, "description") ?: ""

        return orderRepository.existsById(id)
            .flatMap {
                if (it) {
                    event.createFollowup(
                        "An order with the name $id already exists! Please consider using a different " +
                                "name or deleting the old order."
                    )
                } else {
                    orderRepository.save(Order(id, description))
                        .then(event.createFollowup("Created new order $id!"))
                }
            }
    }
}