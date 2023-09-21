package foxparade.command.logic.loot.toy

import foxparade.command.logic.loot.color.BasicColor
import foxparade.command.logic.loot.constants.LootConstants.Companion.COLORABLE_TOYS
import foxparade.command.logic.loot.enums.RarityEnum
import foxparade.command.logic.loot.modifier.Modifier

class ColorableToy(modifier: Modifier) : Toy(modifier) {

    private val color: BasicColor = BasicColor()
    private val toy: String = COLORABLE_TOYS.random()

    override fun getFullToyName(): String {
        return "${if (modifier.getRarity() == RarityEnum.BASIC) "" else color.getColor() + " "}${modifier.getModifier()} $toy"
    }

    override fun getToyName(): String {
        return toy
    }

    override fun getColor(): String {
        return color.getColor()
    }
}