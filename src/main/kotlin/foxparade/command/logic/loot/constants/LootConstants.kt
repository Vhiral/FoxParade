package foxparade.command.logic.loot.constants

import foxparade.command.logic.loot.modifier.*

class LootConstants {
    companion object {

        val MYTHIC_MODIFIERS: List<String> = listOf(
            "Angelic",
            "Elven",
            "Birthday",
            "Halloween",
            "Maple Syrup-y"
        )

        val RARE_MODIFIERS: List<String> = listOf(
            "Haunted",
            "Enchanted",
            "Mysterious",
            "Mystical",
            "Sensual",
            "Mermaid",
        )

        val NORMAL_MODIFIERS: List<String> = listOf(
            "Baby",
            "Beautiful",
            "Bedazzled",
            "Casual",
            "Charming",
            "Colourful",
            "Creepy",
            "Cute",
            "Delicate",
            "Edgy",
            "Elegant",
            "Embroidered",
            "Fairytale",
            "Fashionable",
            "Floral",
            "Fluffy",
            "Frilly",
            "Girly",
            "Gorgeous",
            "Graceful",
            "Kawaii",
            "Lacy",
            "Leather",
            "Long",
            "Oversized",
            "Plaid",
            "Ruffled",
            "Sakura-patterned",
            "Sheer",
            "Short",
            "Sparkling",
            "Spooky",
            "Sunflower-patterned",
            "Unique",
            "Vintage",
            "Starry",
            "Cloudy",
        )

        val BAD_MODIFIERS: List<String> = listOf(
            "Broken",
            "Creepy",
            "Furry",
            "Loose",
            "Mismatched",
            "Mouldy",
            "Overpriced",
            "Stained",
            "Sticky",
            "Stinky",
            "Tattered",
            "Wet",
            "Yellowed",
            "Chewed-on",
        )

        val BASIC_COLORS: List<String> = listOf(
            "Pastel",
            "Red",
            "Blue",
            "Green",
            "Yellow",
            "Orange",
            "Purple",
            "Pink",
            "Brown",
            "Black",
            "White",
            "Grey",
            "Pearlescent",
        )

        val ANIMAL_COLORS: List<String> = listOf(
            "Froggy",
            "Bear",
            "Cat",
            "Puppy",
            "Fox",
            "Mouse",
            "Hamster",
            "Wolf",
            "Tanuki",
            "Bunny",
        )

        val BODY_COLORS: List<String> = listOf(
            "Tan",
            "Flesh",
            "Semi-white",
            "Tawny"
        )

        val COLORABLE_TOYS: List<String> = listOf(
            "Outfit",
            "Wig",
            "Eyes",
            "Shoes",
            "Hair Clips",
            "Doll Bag",
            "Necklace",
            "Glasses",
            "Headphones",
            "Pacifier",
            "Hat",
            "Socks",
            "Lingerie",
            "Swimsuit",
            "Kimono",
            "School Uniform",
            "Lolita Dress",
            "Punk Outfit",
            "Jirai-kei Outfit",
            "Fantasy Outfit",
            "Maid Outfit",
            "Nurse Uniform",
            "Miko Outfit",
            "Handbag",
            "Backpack",
            "Umbrella",
            "Raincoat",
            "Boots",
            "Roller Skates",
            "Crocs",
            "Pyjamas",
            "Panties",
            "Skirt",
            "Shorts",
            "Overalls",
            "Tutu",
            "Beret",
            "Uniform",
            "Shirt",
            "Mini Skirt",
            "Cheerleader Outfit",
            "Ita-bag",
            "Hoodie",
            "Sweater",
            "Apron",
            "Bikini",
            "High Heels",
            "Stockings",
            "Platform Shoes",
            "Choker",
            "Garters",
        )

        val ANIMAL_TOYS: List<String> = listOf(
            "Tail",
            "Ears",
            "Paws",
            "Hat",
            "Socks",
            "Lingerie",
            "School Uniform",
            "Lolita Dress",
            "Punk Outfit",
            "Maid Outfit",
            "Nurse Uniform",
            "Miko Outfit",
            "Handbag",
            "Backpack",
            "Umbrella",
            "Raincoat",
            "Boots",
            "Shoes",
            "Onesie",
            "Roller skates",
            "Pyjamas",
            "Panties",
            "Overalls",
            "Uniform",
        )

        val BODY_TOYS: List<String> = listOf(
            "Mini Body",
            "Mini Head",
            "Sister Head",
            "Sister Body",
            "Manicured Hands",
        )

        val MODIFIER_ODDS_TABLE: Map<Long, List<Pair<IntRange, () -> Modifier>>> =
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
    }
}