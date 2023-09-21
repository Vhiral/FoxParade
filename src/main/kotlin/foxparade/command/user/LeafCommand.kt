package foxparade.command.user

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.InteractionFollowupCreateMono
import foxparade.command.BasicCommand
import foxparade.command.logic.line.LeafRandomLine
import foxparade.mongo.CooldownRepository
import foxparade.mongo.LeafRepository
import foxparade.mongo.model.Cooldown
import foxparade.util.TimeUtil
import foxparade.util.TimeUtil.Companion.getCooldownString
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class LeafCommand(
    val leafRepository: LeafRepository,
    val cooldownRepository: CooldownRepository,
    val leafRandomLine: LeafRandomLine
) : BasicCommand() {

    val cooldownMs: Long = 3600000

    val rareDropOdds: IntRange = (95..99)
    val normalDropOdds: IntRange = (0..94)

    val normalDrop: IntRange = (10..30)
    val rareDrop: IntRange = (30..100)

    override fun getName(): String {
        return "leaf"
    }

    override fun createFollowup(event: ChatInputInteractionEvent): Mono<Any> {
        val now: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))

        val cooldownExists: Mono<Boolean> = cooldownRepository.existsById(
            Cooldown.CooldownId(
                event.interaction.user.id.asLong(),
                "leaf"
            )
        )

        return cooldownExists.flatMap { exists ->
            if (exists) {
                val cooldown: Mono<Cooldown> = cooldownRepository.findById(
                    Cooldown.CooldownId(
                        event.interaction.user.id.asLong(),
                        "leaf"
                    )
                )

                cooldown.flatMap { cd ->
                    if (TimeUtil.isOnCooldown(cd.lastPull, now, cooldownMs)) {
                        getCooldownFollowupEvent(event, cd, now)
                    } else {
                        getLeaves(event, now)
                    }
                }
            } else {
                getLeaves(event, now)
            }
        }
    }

    private fun getLeaves(event: ChatInputInteractionEvent, now: LocalDateTime): Mono<Any> {
        val loot: Long = getLoot()

        return leafRepository.incrementUserLeaves(event.interaction.user.id.asLong(), loot)
            .flatMap {
                cooldownRepository.save(
                    Cooldown(
                        Cooldown.CooldownId(
                            event.interaction.user.id.asLong(),
                            "leaf"
                        ), now
                    )
                )
            }
            .flatMap {
                event.createFollowup(
                    leafRandomLine.getRandomLine()
                        .format(event.interaction.user.username, loot)
                )
            }
    }

    private fun getCooldownFollowupEvent(
        event: ChatInputInteractionEvent,
        cd: Cooldown,
        now: LocalDateTime
    ): InteractionFollowupCreateMono {
        return event.createFollowup(
            "${event.interaction.user.username} you are currently on cooldown and can get leaves in " +
                    getCooldownString(cd.lastPull, now, cooldownMs)
        )
    }

    private fun getLoot(): Long {
        return if (normalDropOdds.contains((0..99).random())) {
            normalDrop.random().toLong()
        } else {
            rareDrop.random().toLong()
        }
    }
}