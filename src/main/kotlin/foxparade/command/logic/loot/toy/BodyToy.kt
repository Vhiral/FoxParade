package foxparade.command.logic.loot.toy

import foxparade.command.logic.loot.color.BodyColor
import foxparade.command.logic.loot.constants.LootConstants.Companion.BODY_TOYS
import foxparade.command.logic.loot.enums.RarityEnum
import foxparade.command.logic.loot.modifier.Modifier

class BodyToy(modifier: Modifier) : Toy(modifier) {

    private val color = BodyColor()
    private val toy: String = BODY_TOYS.random()

    override fun getFullToyName(): String {
        return "${if (modifier.getRarity() == RarityEnum.BASIC) "" else modifier.getModifier() + " "}${color.getColor()} $toy"
    }

    override fun getToyName(): String {
        return toy
    }

    override fun getColor(): String {
        return color.getColor()
    }
}