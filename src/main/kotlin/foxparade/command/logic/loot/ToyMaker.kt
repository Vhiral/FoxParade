package foxparade.command.logic.loot

import foxparade.command.logic.loot.constants.LootConstants.Companion.ANIMAL_TOYS
import foxparade.command.logic.loot.constants.LootConstants.Companion.BODY_TOYS
import foxparade.command.logic.loot.constants.LootConstants.Companion.COLORABLE_TOYS
import foxparade.command.logic.loot.modifier.*
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

    private final val modifierOddsTable: Map<Long, List<Pair<IntRange, () -> Modifier>>> =
        mapOf(
            Pair(
                10, listOf(
                    Pair((0 until 25), ::BadModifier),
                    Pair((25 until 75), ::BasicModifier),
                    Pair((75 until 97), ::NormalModifier),
                    Pair((97 until 100), ::RareModifier),
                )
            ),
            Pair(
                20, listOf(
                    Pair((0 until 20), ::BadModifier),
                    Pair((20 until 55), ::BasicModifier),
                    Pair((55 until 90), ::NormalModifier),
                    Pair((90 until 99), ::RareModifier),
                    Pair((99 until 100), ::MythicModifier),
                )
            ),
            Pair(
                50, listOf(
                    Pair((0 until 10), ::BadModifier),
                    Pair((10 until 25), ::BasicModifier),
                    Pair((25 until 73), ::NormalModifier),
                    Pair((73 until 97), ::RareModifier),
                    Pair((97 until 100), ::MythicModifier),
                )
            ),
            Pair(
                100, listOf(
                    Pair((0 until 61), ::NormalModifier),
                    Pair((61 until 93), ::RareModifier),
                    Pair((93 until 100), ::MythicModifier),
                )
            )
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
                modifierOddsTable[leaves]!!
                    .first { it.first.contains(roll) }
                    .second.invoke()
            )
    }
}