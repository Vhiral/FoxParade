package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.constants.LootConstants.Companion.MYTHIC_MODIFIERS
import foxparade.command.logic.loot.enums.RarityEnum

class MythicModifier : Modifier() {

    private val modifier: String = MYTHIC_MODIFIERS.random()

    override fun getModifier(): String {
        return modifier
    }

    override fun getRarity(): RarityEnum {
        return RarityEnum.MYTHIC

    }
}