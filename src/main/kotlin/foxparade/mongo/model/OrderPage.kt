package foxparade.mongo.model

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneId

@Document("orderPage")
@Getter
data class OrderPage(
    @Id
    var id: String,
    var order: String,
    var userName: String,
    var publicName: String?,
    var userId: Long,
    var created: LocalDateTime,
    val adminFlag: Boolean?
) {
    constructor(order: String, userName: String, publicName: String?, userId: Long, adminFlag: Boolean?) : this(
        getRandomString(),
        order,
        userName,
        publicName,
        userId,
        LocalDateTime.now(ZoneId.of("Asia/Tokyo")),
        adminFlag
    )

    companion object {
        private fun getRandomString(): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..20)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}