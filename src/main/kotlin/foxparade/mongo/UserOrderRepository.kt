package foxparade.mongo

import foxparade.mongo.model.UserOrder
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux


interface UserOrderRepository : ReactiveMongoRepository<UserOrder, UserOrder.UserOrderId> {

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'id.orderId':  ?0}}",
        ]
    )
    fun findAllByOrderId(orderId: String): Flux<UserOrder>
}