package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.constants.LootConstants.Companion.BAD_MODIFIERS
import foxparade.command.logic.loot.enums.RarityEnum

class BadModifier : Modifier() {

    private val modifier: String = BAD_MODIFIERS.random()

    override fun getModifier(): String {
        return modifier
    }

    override fun getRarity(): RarityEnum {
        return RarityEnum.BAD
    }
}