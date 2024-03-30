package foxparade.mongo.model

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneId

@Document("order")
@Getter
data class Order(
    @Id
    val id: String,
    var isActive: Boolean,
    var description: String,
    var created: LocalDateTime
) {
    constructor(id: String, description: String) : this(id, false, description, LocalDateTime.now(ZoneId.of("UTC")))
}