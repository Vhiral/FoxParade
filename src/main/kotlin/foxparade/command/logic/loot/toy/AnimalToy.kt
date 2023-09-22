package foxparade.command.logic.loot.toy

import foxparade.command.logic.loot.color.AnimalColor
import foxparade.command.logic.loot.color.BasicColor
import foxparade.command.logic.loot.constants.LootConstants
import foxparade.command.logic.loot.enums.RarityEnum
import foxparade.command.logic.loot.modifier.Modifier

class AnimalToy(modifier: Modifier) : Toy(modifier) {

    private val color = BasicColor()
    private val animal = AnimalColor()
    private val toy: String = LootConstants.ANIMAL_TOYS.random()

    override fun getFullToyName(): String {
        return "${color.getColor()} ${if (modifier.getRarity() == RarityEnum.BASIC) "" else modifier.getModifier() + " "}${animal.getColor()} $toy"
    }

    override fun getToyName(): String {
        return "${animal.getColor()} $toy"
    }

    override fun getColor(): String {
        return color.getColor()
    }
}