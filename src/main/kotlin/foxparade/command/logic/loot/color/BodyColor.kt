package foxparade.command.logic.loot.color

import foxparade.command.logic.loot.constants.LootConstants.Companion.BODY_COLORS

class BodyColor : Color {

    private val color = BODY_COLORS.random()

    override fun getColor(): String {
        return color
    }
}