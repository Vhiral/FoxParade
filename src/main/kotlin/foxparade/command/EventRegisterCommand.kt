package foxparade.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.logic.CommandRegistrar
import mu.KotlinLogging
import reactor.core.publisher.Mono

abstract class EventRegisterCommand(
    private val commandRegistrar: CommandRegistrar
) : SlashCommand {

    private val logger = KotlinLogging.logger {}

    abstract fun createFollowup(event: ChatInputInteractionEvent): Mono<Any>

    override fun handle(event: ChatInputInteractionEvent) {
        event.deferReply()
            .then(createFollowup(event))
            .doOnNext { commandRegistrar.run(null) }
            .doOnError { logger.error { it } }
            .onErrorResume { event.createFollowup("Sorry some error happened...") }
            .subscribe()

    }
}