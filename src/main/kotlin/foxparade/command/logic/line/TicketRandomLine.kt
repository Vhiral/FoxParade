package foxparade.command.logic.line

import org.springframework.stereotype.Component

@Component
class TicketRandomLine : RandomLine(randomLineOdds = 10) {

    private val ticketLine: String = ":tickets: %s :tickets:"
    private val defaultLine: String = "*Pancake gives %s a ticket.*\n"
    private val lines: List<String> = listOf(
        "*Pancake gives %s a ticket. It\'s sticky...*\n",
        "*Pancake gives %s a ticket. It\'s covered in saliva...*\n",
        "*Pancake gives %s a ticket. This one has been chewed on.*\n",
        "*Pancake gives %s a golden ticket!? Wait... It\'s just yellow crayon.*\n",
        "*Pancake is too busy playing with his toys to give %s a ticket. So you take one yourself.*\n"
    )

    override fun getDefaultLine(): String {
        return defaultLine + ticketLine
    }

    override fun getLine(): String {
        return lines.random() + ticketLine
    }
}