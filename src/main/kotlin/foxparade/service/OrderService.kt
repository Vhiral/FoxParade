package foxparade.service

import foxparade.controller.dto.UpdateOrderRequest
import foxparade.mongo.OrderPageRepository
import foxparade.mongo.OrderRepository
import foxparade.mongo.UserOrderRepository
import foxparade.mongo.model.Order
import foxparade.mongo.model.OrderPage
import foxparade.mongo.model.UserOrder
import kotlinx.coroutines.reactor.mono
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val orderPageRepository: OrderPageRepository,
    val userOrderRepository: UserOrderRepository
) {

    fun getOrderView(pageId: String): Mono<Rendering> {
        return getOrderPage(pageId, orderPageRepository::findById)
            .flatMap { orderPage ->
                orderRepository.findById(orderPage.order)
                    .flatMap { order ->
                        getUserOrderDescription(orderPage, order)
                            .flatMap { userOrderDescription ->
                                Mono.just(
                                    Rendering.view("order")
                                        .modelAttribute("updateOrderRequest", UpdateOrderRequest(userOrderDescription))
                                        .modelAttribute("pageId", pageId)
                                        .modelAttribute("username", orderPage.userName)
                                        .modelAttribute("order", order.id)
                                        .modelAttribute("description", order.description)
                                        .build()
                                )
                            }
                    }
            }
    }

    fun getUpdateView(pageId: String, updateOrderRequest: UpdateOrderRequest): Mono<Rendering> {
        return getOrderPage(pageId, orderPageRepository::findById)
            .flatMap { orderPage ->
                orderRepository.findById(orderPage.order)
                    .flatMap { order ->
                        userOrderRepository.save(UserOrder(orderPage, order, updateOrderRequest.description))
                            .flatMap {
                                Mono.just(
                                    Rendering.view("updateOrder")
                                        .modelAttribute("username", orderPage.userName)
                                        .build()
                                )
                            }
                    }
            }
    }

    fun deleteView(pageId: String): Mono<Rendering> {
        return getOrderPage(pageId, orderPageRepository::findById)
            .flatMap { orderPage ->
                orderRepository.findById(orderPage.order)
                    .flatMap { order ->
                        userOrderRepository.deleteById(UserOrder.UserOrderId(orderPage.userId, order.id))
                            .then(
                                Mono.just(
                                    Rendering.view("deleteOrder")
                                        .modelAttribute("username", orderPage.userName)
                                        .build()
                                )
                            )
                    }
            }
    }

    fun getAdminOrderView(pageId: String): Mono<Rendering> {
        return getOrderPage(pageId, orderPageRepository::findByIdAndAdminFlagIsTrue)
            .flatMap { orderPage ->
                orderRepository.findById(orderPage.order)
                    .flatMap { order ->
                        userOrderRepository.findAllByOrderId(order.id)
                            .collectList()
                            .flatMap { userOrders ->
                                Mono.just(
                                    Rendering.view("adminOrder")
                                        .modelAttribute("orders", userOrders)
                                        .modelAttribute("order", order.id)
                                        .build()
                                )
                            }
                    }
            }
    }

    private fun getUserOrderDescription(
        orderPage: OrderPage,
        order: Order
    ): Mono<String> {
        return userOrderRepository.existsById(UserOrder.UserOrderId(orderPage.userId, order.id))
            .flatMap {
                if (it) {
                    userOrderRepository.findById(UserOrder.UserOrderId(orderPage.userId, order.id))
                        .map { userOrder -> userOrder.description }
                } else {
                    mono {
                        ""
                    }
                }
            }
    }

    fun getOrderPage(pageId: String, getOrderPage: (String) -> Mono<OrderPage>): Mono<OrderPage> {

        return orderPageRepository.existsById(pageId)
            .flatMap {
                if (!it) {
                    Mono.error(NotFoundException())
                } else {
                    getOrderPage.invoke(pageId)
                }
            }
    }

}