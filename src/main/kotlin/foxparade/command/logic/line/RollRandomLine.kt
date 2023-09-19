package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class RollRandomLine : RandomLine() {

    private val odds: Int = 5
    private val rollLine: String = "*Pancake rolls some dice.*"
    private val lines: Map<Int, String> = mapOf(
        Pair(0, "*Pancake meticulously rolls the dice one by one.*"),
        Pair(1, "*Pancake isn\'t paying attention. You roll the dice yourself.*"),
        Pair(2, "*Pancake knocks his cup of dice onto the ground.*"),
        Pair(3, "*Pancake aggressively throws the dice onto the table.*"),
        Pair(4, "*Pancake lazily drops the dice onto the table.*"),
        Pair(5, "*Pancake rolls some dice. ...Did he forget one?*"),
        Pair(6, "*Pancake rolls some dice. It seems like some are still stuck to his fur...*"),
        Pair(7, "*Pancake is asleep behind the table. There are some dice clutched in his paw.*"),
        Pair(8, "*Pancake spits out a couple of dice he was chewing on.*"),
        Pair(9, "*Pancake gives you a ticket. It has a picture of some dice on it.*")
    )

    override fun getDefaultLine(): String {
        return rollLine
    }

    override fun getOddsForSpecialLine(): Int {
        return odds
    }

    override fun getLine(number: Int): String {
        return lines[number]!!
    }

    override fun getSize(): Int {
        return lines.size
    }
}