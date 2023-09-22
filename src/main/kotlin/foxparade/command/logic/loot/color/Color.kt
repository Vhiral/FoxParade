package foxparade.command.logic.loot.color

abstract class Color(
    colors: List<String>
) {

    private val color = colors.random()

    fun getColor(): String {
        return color
    }
}