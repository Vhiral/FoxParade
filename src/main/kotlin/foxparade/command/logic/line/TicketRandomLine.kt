package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class TicketRandomLine : RandomLine() {

    private val odds: Int = 10
    private val ticketLine: String = ":tickets: {ticket} :tickets:"
    private val defaultLine: String = "*Pancake gives %s a ticket.*\n"
    private val lines: Map<Int, String> = mapOf(
        Pair(0, "*Pancake gives %s a ticket. It\'s sticky...*\n"),
        Pair(1, "*Pancake gives %s a ticket. It\'s covered in saliva...*\n"),
        Pair(2, "*Pancake gives %s a ticket. This one has been chewed on.*\n"),
        Pair(3, "*Pancake gives %s a golden ticket!? Wait... It\'s just yellow crayon.*\n"),
        Pair(4, "*Pancake is too busy playing with his toys to give %s a ticket. So you take one yourself.*\n")
    )

    override fun getDefaultLine(): String {
        return defaultLine + ticketLine
    }

    override fun getOddsForSpecialLine(): Int {
        return odds
    }

    override fun getLine(number: Int): String {
        return lines[number]!! + ticketLine
    }

    override fun getSize(): Int {
        return lines.size
    }


}