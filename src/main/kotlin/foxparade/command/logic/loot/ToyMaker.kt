package foxparade.command.logic.loot

import foxparade.command.logic.loot.constants.LootConstants.Companion.ANIMAL_TOYS
import foxparade.command.logic.loot.constants.LootConstants.Companion.BODY_TOYS
import foxparade.command.logic.loot.constants.LootConstants.Companion.COLORABLE_TOYS
import foxparade.command.logic.loot.constants.LootConstants.Companion.MODIFIER_ODDS_TABLE
import foxparade.command.logic.loot.modifier.Modifier
import foxparade.command.logic.loot.toy.AnimalToy
import foxparade.command.logic.loot.toy.BodyToy
import foxparade.command.logic.loot.toy.ColorableToy
import foxparade.command.logic.loot.toy.Toy
import org.springframework.stereotype.Component

@Component
class ToyMaker {

    private final var toyOddsTable: List<Pair<IntRange, (Modifier) -> Toy>>
    private final var toyWeight: Int = 0

    private final val toyList: List<Pair<List<String>, (Modifier) -> Toy>> = listOf(
        Pair(COLORABLE_TOYS, ::ColorableToy),
        Pair(ANIMAL_TOYS, ::AnimalToy),
        Pair(BODY_TOYS, ::BodyToy)
    )

    init {
        toyOddsTable = toyList
            .map {
                val pair = Pair((toyWeight until toyWeight + it.first.size), it.second)
                toyWeight += it.first.size
                pair
            }
    }

    fun rollForLoot(leaves: Long): Toy {
        val roll: Int = (0 until 100).random()
        val toyRoll: Int = (0 until toyWeight - 1).random()

        return toyOddsTable
            .first { it.first.contains(toyRoll) }
            .second.invoke(
                MODIFIER_ODDS_TABLE[leaves]!!
                    .first { it.first.contains(roll) }
                    .second.invoke()
            )
    }
}