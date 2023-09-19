package foxparade.mongo

import foxparade.mongo.model.Cooldown
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CooldownRepository : ReactiveMongoRepository<Cooldown, Cooldown.CooldownId> {

//    fun findByUserIdAndEventId(userId: String, eventId: String): Mono<Cooldown?>

//    fun existsByUserIdAndEventIdAndLastPullIsLessThanEqual(
//        userId: String,
//        eventId: String,
//        lastPull: LocalDateTime
//    ): Mono<Boolean>
}