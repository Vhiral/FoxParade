package foxparade.mongo

import foxparade.mongo.model.Order
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux


interface OrderRepository : ReactiveMongoRepository<Order, String> {

    @Aggregation(
        pipeline = [
            "{ '\$sort':  { 'created':  -1 }}",
            "{ '\$limit': 25 }"
        ]
    )
    override fun findAll(): Flux<Order>

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'isActive':  ?0}}",
            "{ '\$sort':  { 'created':  -1 }}",
            "{ '\$limit': 25 }"
        ]
    )
    fun findAllByAndIsActiveIs(isActive: Boolean = true): Flux<Order>

}