package foxparade.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.time.LocalDateTime

@Document("cooldown")
class Cooldown(
    @Id
    val id: CooldownId,
    val lastPull: LocalDateTime
) {
    data class CooldownId(
        val userId: Long,
        val eventId: String,
    ) : Serializable
}