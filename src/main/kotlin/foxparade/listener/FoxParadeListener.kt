package foxparade.listener

import discord4j.core.DiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import foxparade.command.logic.CommandFactory
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class FoxParadeListener(
    private val client: DiscordClient,
    private val commandFactory: CommandFactory
) {

    private final val logger = KotlinLogging.logger {}

    @PostConstruct
    fun setup() {
        client.withGateway {
            mono {
                it.on(ChatInputInteractionEvent::class.java)
                    .asFlow()
                    .collect {
                        handle(it)
                    }
            }
        }.subscribe()
    }

    fun handle(message: ChatInputInteractionEvent) {
        commandFactory.getSlashCommand(message.commandName)?.handle(message)
            ?: logger.warn { "Cannot find command for: ${message.commandName}." }
    }
}