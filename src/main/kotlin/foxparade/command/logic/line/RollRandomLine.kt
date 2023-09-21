package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class RollRandomLine : RandomLine(randomLineOdds = 5) {

    private val defaultLine: String = "*Pancake rolls some dice.*"
    private val lines: List<String> = listOf(
        "*Pancake meticulously rolls the dice one by one.*",
        "*Pancake isn\'t paying attention. You roll the dice yourself.*",
        "*Pancake knocks his cup of dice onto the ground.*",
        "*Pancake aggressively throws the dice onto the table.*",
        "*Pancake lazily drops the dice onto the table.*",
        "*Pancake rolls some dice. ...Did he forget one?*",
        "*Pancake rolls some dice. It seems like some are still stuck to his fur...*",
        "*Pancake is asleep behind the table. There are some dice clutched in his paw.*",
        "*Pancake spits out a couple of dice he was chewing on.*",
        "*Pancake gives you a ticket. It has a picture of some dice on it.*"
    )

    override fun getDefaultLine(): String {
        return defaultLine
    }

    override fun getLine(): String {
        return lines.random()
    }
}