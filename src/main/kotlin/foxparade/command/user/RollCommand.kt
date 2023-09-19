package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.InteractionFollowupCreateMono
import foxparade.command.DialogCommand
import foxparade.command.logic.line.RollRandomLine
import org.springframework.stereotype.Component

@Component
class RollCommand(val rollRandomLine: RollRandomLine) : DialogCommand() {
    override fun getName(): String {
        return "roll"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): InteractionFollowupCreateMono {
        return event.createFollowup(
            "${rollRandomLine.getRandomLine()}\n"
                    + ":game_die: ${(1..100).random()} :game_die:"
        )
    }
}