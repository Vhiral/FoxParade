package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.constants.LootConstants.Companion.NORMAL_MODIFIERS
import foxparade.command.logic.loot.enums.RarityEnum

class NormalModifier : Modifier() {

    private val modifier: String = NORMAL_MODIFIERS.random()

    override fun getModifier(): String {
        return modifier
    }

    override fun getRarity(): RarityEnum {
        return RarityEnum.NORMAL
    }
}