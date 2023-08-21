package foxparade.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Cooldown(
    val userId: String,
    val eventId: String,
    val lastPull: LocalDateTime
) {
    @Id
    lateinit var id: String
}