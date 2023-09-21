package foxparade.command.logic.loot.color

import foxparade.command.logic.loot.constants.LootConstants.Companion.ANIMAL_COLORS

class AnimalColor : Color {

    private val color = ANIMAL_COLORS.random()

    override fun getColor(): String {
        return color
    }
}