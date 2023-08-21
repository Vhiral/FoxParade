package foxparade.mongo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Ticket")
data class Ticket(
    @Id
    var id: String,
    var eventId: String,
    var userId: String,
    var number: Int
) {
    constructor(eventId: String, userId: String, number: Int) : this(
        id = "${eventId.lowercase()}_${number}",
        eventId = eventId,
        userId = userId,
        number = number,
    )
}