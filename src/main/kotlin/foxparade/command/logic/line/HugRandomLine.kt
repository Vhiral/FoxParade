package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class HugRandomLine : RandomLine(randomLineOdds = 2) {

    private val lines: List<String> = listOf(
        "*%s gives Pancake a hug.*",
        "*%s attempts to hug Pancake, but he squirms and rolls away.*",
        "*%s hugs Pancake. Now you're all sticky!*",
        "*Pancake contemplates the existential crisis of being hugged by invisible forces.*",
        "*%s hugs Pancake. It feels like trying to hug a very soggy pancake... eww.*",
        "*Pancake the fox accepts %s\'s hug, his tail wagging gently.*",
        "*As %s embraces Pancake, they feel something wet and slimy oozing out of his fur...*",
        "*%s tries to hug Pancake but he scampers away.*",
        "*Pancake briefly abandons his Switch game to hug %s.*",
        "*Pancake rolls his dice while %s hugs him, totally confused.*",
        "*Pancake snoozes, unaware of %s\'s hug attempt.*",
        "*%s tries to hug Pancake but they will have to wait; he\'s in the middle of a game of Mario Kart.*",
        "*%s tries to hug Pancake but he\'s got a Jenga tower to balance; maybe later.*",
        "*Pancake acknowledges %s\'s hug with a lazy paw wave.*",
        "*Pancake looks at %s with utter confusion, wondering why they're attempting to squeeze the air out of him!*",
    )

    override fun getDefaultLine(): String {
        return lines.random()
    }

    override fun getLine(): String {
        return lines.random()
    }
}