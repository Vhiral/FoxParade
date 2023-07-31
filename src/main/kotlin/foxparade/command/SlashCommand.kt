package foxparade.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent

interface SlashCommand {

    fun getName(): String
    fun handle(event: ChatInputInteractionEvent)
}