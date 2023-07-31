package foxparade.mongo

import foxparade.mongo.dto.Event
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface EventRepository : ReactiveMongoRepository<Event, String> {

    override fun existsById(id: String): Mono<Boolean>

    @Aggregation(
        pipeline = [
            "{ '\$sort':  { 'startTime':  -1 }}",
            "{ '\$limit': 25 }"
        ]
    )
    override fun findAll(): Flux<Event>
}