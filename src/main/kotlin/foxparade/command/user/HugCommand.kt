package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.SlashCommand
import org.springframework.stereotype.Component

@Component
class HugCommand : SlashCommand {

    override fun getName(): String {
        return "hug"
    }

    override fun handle(event: ChatInputInteractionEvent) {
        event.deferReply()
            .then(event.createFollowup("Aaaa~"))
            .subscribe()

    }
}