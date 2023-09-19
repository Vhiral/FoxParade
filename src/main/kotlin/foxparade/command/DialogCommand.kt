package foxparade.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.InteractionFollowupCreateMono
import mu.KotlinLogging

abstract class DialogCommand : SlashCommand {

    private val logger = KotlinLogging.logger {}

    abstract fun createFollowup(event: ChatInputInteractionEvent): InteractionFollowupCreateMono

    override fun handle(event: ChatInputInteractionEvent) {
        event.deferReply()
            .then(createFollowup(event))
            .doOnError { logger.error { it } }
            .onErrorResume { event.createFollowup("Sorry, some error happened...") }
            .subscribe()
    }
}