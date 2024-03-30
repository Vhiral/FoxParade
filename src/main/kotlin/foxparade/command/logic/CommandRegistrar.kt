package foxparade.command.logic

import discord4j.common.JacksonResources
import discord4j.core.DiscordClient
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ApplicationCommandRequest
import foxparade.mongo.EventRepository
import foxparade.mongo.OrderRepository
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


@Component
class CommandRegistrar(
    val client: DiscordClient,
    val eventRepository: EventRepository,
    val orderRepository: OrderRepository
) : ApplicationRunner {

    private final val d4jMapper: JacksonResources = JacksonResources.create()
    private final val logger = KotlinLogging.logger {}
    private final val adminEventInjectionTargets: Set<String> = setOf(
        "activate_event",
        "set_cooldown"
    )
    private final val adminOrderInjectionTargets: Set<String> = setOf(
        "update_order",
        "admin_order"
    )
    private final val userEventInjectionTargets: Set<String> = setOf(
        "ticket",
        "my_tickets"
    )
    private final val userOrderInjectionTargets: Set<String> = setOf(
        "order"
    )

    override fun run(args: ApplicationArguments?) {

        val commands: List<ApplicationCommandRequest> = getCommands()

        injectChoicesIntoTarget(commands, adminEventInjectionTargets, eventRepository.findAll().map { it.id }, "event")
        injectChoicesIntoTarget(commands, adminOrderInjectionTargets, orderRepository.findAll().map { it.id }, "order")
        injectChoicesIntoTarget(
            commands,
            userEventInjectionTargets,
            eventRepository.findAllByAndIsActiveIs().map { it.id },
            "event"
        )
        injectChoicesIntoTarget(
            commands,
            userOrderInjectionTargets,
            orderRepository.findAllByAndIsActiveIs().map { it.id },
            "order"
        )

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

    private fun injectChoicesIntoTarget(
        commands: List<ApplicationCommandRequest>,
        targets: Set<String>,
        ids: Flux<String>,
        optionName: String
    ) {
        commands.forEach {
            if (!it.options().isAbsent && targets.contains(it.name())) {
                it.options().get()
                    .filter { option -> option.name() == optionName }
                    .forEach { option ->
                        ids.doOnNext { id ->
                            option.choices().get()
                                .add(
                                    ApplicationCommandOptionChoiceData.builder()
                                        .name(id)
                                        .value(id)
                                        .build()
                                )
                        }
                            .blockLast()
                    }
            }
        }
    }
}