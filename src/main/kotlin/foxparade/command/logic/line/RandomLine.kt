package foxparade.command.logic.line

abstract class RandomLine {

    protected abstract fun getDefaultLine(): String
    protected abstract fun getOddsForSpecialLine(): Int
    protected abstract fun getLine(number: Int): String
    protected abstract fun getSize(): Int

    fun getRandomLine(): String {
        return if ((0 until getOddsForSpecialLine() - 1).random() == 0)
            getLine((0 until getSize() - 1).random())
        else
            getDefaultLine()

    }
}