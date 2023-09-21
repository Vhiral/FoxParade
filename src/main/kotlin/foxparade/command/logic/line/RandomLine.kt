package foxparade.command.logic.line

abstract class RandomLine(
    private val randomLineOdds: Int
) {

    protected abstract fun getDefaultLine(): String
    protected abstract fun getLine(): String

    fun getRandomLine(): String {
        return if ((0 until randomLineOdds - 1).random() == 0)
            getLine()
        else
            getDefaultLine()
    }
}