package foxparade.controller

import foxparade.controller.dto.UpdateOrderRequest
import foxparade.service.OrderService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class OrderController(
    val orderService: OrderService
) {

    @GetMapping("/order/{pageId}")
    fun getPageId(@PathVariable pageId: String): Mono<Rendering> {
        return orderService.getOrderView(pageId)
    }

    @PostMapping("/order/{pageId}/update")
    fun updateOrder(
        @PathVariable pageId: String,
        @ModelAttribute updateOrderRequest: UpdateOrderRequest
    ): Mono<Rendering> {
        return orderService.getUpdateView(pageId, updateOrderRequest)
    }

    @PostMapping("/order/{pageId}/delete")
    fun deleteOrder(@PathVariable pageId: String): Mono<Rendering> {
        return orderService.deleteView(pageId)
    }

    @GetMapping("/order/admin/{pageId}")
    fun getAdminPage(@PathVariable pageId: String): Mono<Rendering> {
        return orderService.getAdminOrderView(pageId)
    }
}