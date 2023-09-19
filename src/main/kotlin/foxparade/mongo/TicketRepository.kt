package foxparade.mongo

import foxparade.mongo.model.Ticket
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketRepository : ReactiveMongoRepository<Ticket, String> {

    @Aggregation(
        pipeline = [
            "{ '\$match':  {'eventId':  ?0}}",
            "{ '\$sort':  { 'number':  -1 }}"
        ]
    )
    fun findByEventId(eventId: String): Mono<Ticket>

    fun findAllByEventIdAndUserId(eventId: String, userId: Long): Flux<Ticket>
    fun existsByEventIdAndUserId(eventId: String, userId: Long): Mono<Boolean>
    fun existsByEventId(eventId: String): Mono<Boolean>
}