package foxparade.mongo.model

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Document("event")
@Getter
data class Event(
    @Id
    val id: String,
    var isActive: Boolean,
    var cooldown: Long,
    var startTime: LocalDateTime
) {
    constructor(id: String, startTime: ZonedDateTime) : this(id, false, 600, startTime.toLocalDateTime())
}