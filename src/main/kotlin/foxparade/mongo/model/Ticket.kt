package foxparade.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("ticket")
data class Ticket(
    @Id
    var id: String,
    var eventId: String,
    var userId: Long,
    var number: Int
) {
    constructor(eventId: String, userId: Long, number: Int) : this(
        id = "${eventId.lowercase()}_${number}",
        eventId = eventId,
        userId = userId,
        number = number,
    )
}