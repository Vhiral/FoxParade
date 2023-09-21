package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.enums.RarityEnum

abstract class Modifier {

    abstract fun getModifier(): String
    abstract fun getRarity(): RarityEnum
}