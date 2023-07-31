package foxparade.command.logic

import foxparade.command.SlashCommand
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class CommandFactory(private val commands: List<SlashCommand>) {

    private final val commandsMap: MutableMap<String, SlashCommand> = mutableMapOf()

    @PostConstruct
    fun setup() {
        commands.forEach { commandsMap[it.getName()] = it }
    }

    fun getSlashCommand(command: String): SlashCommand? {
        return commandsMap.getOrDefault(command, null)
    }
}