package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.InteractionFollowupCreateMono
import foxparade.command.DialogCommand
import foxparade.command.logic.line.HugRandomLine
import org.springframework.stereotype.Component

@Component
class HugCommand(
    private val hugRandomLine: HugRandomLine
) : DialogCommand() {

    override fun getName(): String {
        return "hug"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): InteractionFollowupCreateMono {
        return event.createFollowup(
            hugRandomLine.getRandomLine().format(event.interaction.user.username)
        )
    }
}