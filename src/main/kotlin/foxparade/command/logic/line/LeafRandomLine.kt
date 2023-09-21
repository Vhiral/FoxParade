package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class LeafRandomLine : RandomLine(randomLineOdds = 5) {

    private val defaultLine: String = "*Pancake hands %s %s leaves.*"
    private val lines: List<String> = listOf(
        "*They\'re shaped like leafy hearts ... aww.*",
        "*A few of them have bite marks.*",
        "*They\'re still damp.*",
        "*They\'re very crunchy.*",
        "*They\'re oddly heavy.*",
        "*One of them is cut out in the shape of a fox*",
        "*They\'re all sticky ... Are they covered in maple syrup?*",
        "*They smell like dirt.*",
        "*Ouch! There are all thorny.*",
    )


    override fun getDefaultLine(): String {
        return defaultLine
    }

    override fun getLine(): String {
        return defaultLine + "\n" + lines.random()
    }
}