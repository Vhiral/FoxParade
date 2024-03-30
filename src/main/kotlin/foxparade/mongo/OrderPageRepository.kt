package foxparade.mongo

import foxparade.mongo.model.OrderPage
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface OrderPageRepository : ReactiveMongoRepository<OrderPage, String> {

    fun findByIdAndAdminFlagIsTrue(id: String): Mono<OrderPage>

}