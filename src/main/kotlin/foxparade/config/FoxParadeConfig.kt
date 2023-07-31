package foxparade.config

import discord4j.core.DiscordClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FoxParadeConfig {

    @Bean
    fun discordClientConfig(@Value("\${discord.token}") token: String): DiscordClient {
        return DiscordClient.builder(token)
            .build()
    }
}