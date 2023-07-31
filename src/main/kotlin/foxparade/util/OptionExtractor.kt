package foxparade.util

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationCommandInteractionOption
import discord4j.core.`object`.command.ApplicationCommandInteractionOptionValue
import java.util.function.Function

class OptionExtractor {

    companion object {

        fun extractStringOption(event: ChatInputInteractionEvent, option: String): String {
            return extractTOption(event, option, ApplicationCommandInteractionOptionValue::asString)
        }

        fun extractBooleanOption(event: ChatInputInteractionEvent, option: String): Boolean {
            return extractTOption(event, option, ApplicationCommandInteractionOptionValue::asBoolean)
        }

        fun extractLongOption(event: ChatInputInteractionEvent, option: String): Long {
            return extractTOption(event, option, ApplicationCommandInteractionOptionValue::asLong)
        }

        private fun <T> extractTOption(
            event: ChatInputInteractionEvent,
            option: String,
            function: Function<ApplicationCommandInteractionOptionValue, T>
        ): T {
            return event.getOption(option)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(function::apply)
                .get()
        }
    }
}