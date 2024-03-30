package foxparade.mongo.model

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document("userOrder")
@Getter
data class UserOrder(
    @Id
    val id: UserOrderId,
    val publicName: String?,
    val userName: String,
    var description: String,
    var check: Boolean
) {

    constructor(orderPage: OrderPage, order: Order, description: String) : this(
        UserOrderId(orderPage.userId, order.id),
        orderPage.publicName,
        orderPage.userName,
        description,
        false
    )

    data class UserOrderId(
        val userId: Long,
        val orderId: String
    ) : Serializable
}