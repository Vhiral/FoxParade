package foxparade.mongo

import foxparade.mongo.model.Cooldown
import foxparade.mongo.model.Ticket
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface TicketRepository : ReactiveMongoRepository<Ticket, String> {

    @Aggregation(
        pipeline = [
            "{ '\$sort':  { 'number':  -1 }}",
        ]
    )
    fun findByEventId(eventId: String): Mono<Ticket?>
}