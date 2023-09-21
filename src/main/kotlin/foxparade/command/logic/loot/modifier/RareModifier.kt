package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.constants.LootConstants.Companion.RARE_MODIFIERS
import foxparade.command.logic.loot.enums.RarityEnum

class RareModifier : Modifier() {

    private val modifier: String = RARE_MODIFIERS.random()

    override fun getModifier(): String {
        return modifier
    }

    override fun getRarity(): RarityEnum {
        return RarityEnum.RARE
    }
}