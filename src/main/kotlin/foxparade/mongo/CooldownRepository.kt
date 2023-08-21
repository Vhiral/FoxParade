package foxparade.mongo

import foxparade.mongo.model.Cooldown
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface CooldownRepository : ReactiveMongoRepository<Cooldown, String> {

    fun findByUserIdAndEventId(userId: String, eventId: String): Mono<Cooldown?>

    fun existsByUserIdAndEventIdAndLastPullIsLessThanEqual(
        userId: String,
        eventId: String,
        lastPull: LocalDateTime
    ): Mono<Boolean>
}