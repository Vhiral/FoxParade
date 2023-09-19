package foxparade.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import mu.KotlinLogging
import reactor.core.publisher.Mono

abstract class BasicCommand : SlashCommand {

    private val logger = KotlinLogging.logger {}

    abstract fun createFollowup(event: ChatInputInteractionEvent): Mono<Any>

    override fun handle(event: ChatInputInteractionEvent) {
        event.deferReply()
            .then(createFollowup(event))
            .doOnError { logger.error { it } }
            .onErrorResume { event.createFollowup("Sorry, some error happened...") }
            .subscribe()
    }
}