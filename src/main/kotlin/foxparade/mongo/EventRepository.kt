package foxparade.mongo

import foxparade.mongo.model.Event
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime


interface EventRepository : ReactiveMongoRepository<Event, String> {

    override fun existsById(id: String): Mono<Boolean>
    fun getByIdAndStartTimeIsLessThanEqual(id: String, timeNow: LocalDateTime): Mono<Event>
    fun existsByIdAndStartTimeIsLessThanEqualAndIsActiveIs(
        id: String,
        timeNow: LocalDateTime,
        isActive: Boolean = true
    ): Mono<Boolean>

    fun existsByIdAndIsActiveIs(id: String, isActive: Boolean = true): Mono<Boolean>

    @Aggregation(
        pipeline = [
            "{ '\$sort':  { 'startTime':  -1 }}",
            "{ '\$limit': 25 }"
        ]
    )
    override fun findAll(): Flux<Event>

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'isActive':  ?0}}",
            "{ '\$sort':  { 'startTime':  -1 }}",
            "{ '\$limit': 25 }"
        ]
    )
    fun findAllByAndIsActiveIs(isActive: Boolean = true): Flux<Event>
}