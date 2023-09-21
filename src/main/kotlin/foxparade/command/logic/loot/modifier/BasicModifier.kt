package foxparade.command.logic.loot.modifier

import foxparade.command.logic.loot.enums.RarityEnum

class BasicModifier : Modifier() {

    override fun getModifier(): String {
        return ""
    }

    override fun getRarity(): RarityEnum {
        return RarityEnum.BASIC
    }
}