package foxparade.command.logic

import discord4j.common.JacksonResources
import discord4j.core.DiscordClient
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ApplicationCommandRequest
import foxparade.mongo.EventRepository
import foxparade.mongo.model.Event
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


@Component
class CommandRegistrar(
    val client: DiscordClient,
    val eventRepository: EventRepository
) : ApplicationRunner {

    private final val d4jMapper: JacksonResources = JacksonResources.create()
    private final val logger = KotlinLogging.logger {}
    private final val adminEventInjectionTargets: Set<String> = setOf(
        "activate_event",
        "set_cooldown",
    )
    private final val userEventInjectionTargets: Set<String> = setOf(
        "ticket",
        "my_tickets"
    )

    override fun run(args: ApplicationArguments?) {

        val commands: List<ApplicationCommandRequest> = getCommands()

        injectEventChoices(commands, adminEventInjectionTargets, eventRepository.findAll())
        injectEventChoices(commands, userEventInjectionTargets, eventRepository.findAllByAndIsActiveIs())

        overwriteApplicationCommands(commands)
    }

    private fun overwriteApplicationCommands(commands: List<ApplicationCommandRequest>) {
        val applicationId: Long? = client.applicationId.block()
        client.applicationService.bulkOverwriteGlobalApplicationCommand(applicationId!!, commands)
            .doOnNext { logger.info { "Registered global commands!" } }
            .doOnError { logger.error { "Error while registering global commands!" } }
            .subscribe()
    }

    private fun getCommands(): List<ApplicationCommandRequest> {
        return PathMatchingResourcePatternResolver().getResources("commands/*.json")
            .map { d4jMapper.objectMapper.readValue(it.inputStream, ApplicationCommandRequest::class.java) }
            .toList()
    }

    private fun injectEventChoices(
        commands: List<ApplicationCommandRequest>,
        targets: Set<String>,
        events: Flux<Event>
    ) {
        commands.forEach {
            if (!it.options().isAbsent && targets.contains(it.name())) {
                it.options().get()
                    .filter { option -> option.name() == "event" }
                    .forEach { option ->
                        events.doOnNext { event ->
                            option.choices().get()
                                .add(
                                    ApplicationCommandOptionChoiceData.builder()
                                        .name(event.id)
                                        .value(event.id)
                                        .build()
                                )
                        }
                            .blockLast()
                    }
            }
        }
    }
}