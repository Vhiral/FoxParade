package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.Message
import foxparade.command.BasicCommandEphemeral
import foxparade.mongo.OrderPageRepository
import foxparade.mongo.OrderRepository
import foxparade.mongo.model.Order
import foxparade.mongo.model.OrderPage
import foxparade.util.OptionExtractor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class OrderCommand(
    val orderRepository: OrderRepository,
    val orderPageRepository: OrderPageRepository,
    @Value("\${foxparade.domain.name}")
    val domainName: String
) : BasicCommandEphemeral() {
    override fun getName(): String {
        return "order"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val orderId: String = OptionExtractor.extractStringOption(event, "order").lowercase()

        val orderExists: Mono<Boolean> = orderRepository.existsById(orderId)

        return orderExists
            .flatMap { getOrder(it, event, orderId) }
            .flatMap { createOrderPage(it, event) }
    }

    private fun getOrder(
        orderExists: Boolean,
        event: ChatInputInteractionEvent,
        orderId: String
    ): Mono<Order> {
        return if (!orderExists) {
            event.createFollowup(
                "The order $orderId seems to either not exist or has not started yet!"
            )
            Mono.empty()
        } else {
            orderRepository.findById(orderId)
        }
    }

    private fun createOrderPage(
        order: Order,
        event: ChatInputInteractionEvent
    ): Mono<Message> {
        val orderPage = OrderPage(order.id, event.interaction.user.username, event.interaction.user.globalName.orElse(null), event.interaction.user.id.asLong(), false)

        return orderPageRepository.save(orderPage)
            .then(
                event.createFollowup(
                    "*Pancake hands you a leaf and beckons you to write your order down*\n" +
                            "$domainName/order/${orderPage.id}"
                )
            )
    }
}