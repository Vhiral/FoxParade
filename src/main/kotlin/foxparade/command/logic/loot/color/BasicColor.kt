package foxparade.command.logic.loot.color

import foxparade.command.logic.loot.constants.LootConstants.Companion.BASIC_COLORS

class BasicColor : Color {

    private val color = BASIC_COLORS.random()

    override fun getColor(): String {
        return color
    }
}