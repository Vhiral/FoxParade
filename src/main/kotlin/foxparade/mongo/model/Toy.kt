package foxparade.mongo.model

import foxparade.command.logic.loot.enums.RarityEnum
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("toy")
data class Toy(
    private val owner: Long,
    private val fullToyName: String,
    private val toy: String,
    private val color: String?,
    private val modifier: String?,
    private val rarity: RarityEnum,
    private val created: LocalDateTime
)
