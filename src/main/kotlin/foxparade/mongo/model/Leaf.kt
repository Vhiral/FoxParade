package foxparade.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("leaf")
data class Leaf(
    @Id
    val id: Long,
    val leaves: Long
)