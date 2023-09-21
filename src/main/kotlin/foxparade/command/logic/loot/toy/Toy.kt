package foxparade.command.logic.loot.toy

import foxparade.command.logic.loot.enums.RarityEnum
import foxparade.command.logic.loot.modifier.Modifier


abstract class Toy(
    protected val modifier: Modifier
) {

    abstract fun getFullToyName(): String
    abstract fun getToyName(): String
    abstract fun getColor(): String?

    fun getModifier(): String {
        return modifier.getModifier()
    }

    fun getRarity(): RarityEnum {
        return modifier.getRarity()
    }

}