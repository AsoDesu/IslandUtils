package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.details
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.timeElapsed
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext
import dev.asodesu.islandutils.api.game.gameManager

class Fishing(val island: String, types: List<String>) : Game("fishing", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images(island, "fishing")
        details("Fishing in ${islandNames[island]}")
        timeElapsed(gameManager.lastGameChange)
    }

    override fun toString(): String {
        return "Fishing(island=$island)"
    }

    companion object : GameContext {
        override val id: String = "fishing"
        private val temperatures = listOf("temperate", "tropical", "barren")
        val islandNames = mapOf(
            "temperate_1" to "Verdant Woods",
            "temperate_2" to "Floral Forest",
            "temperate_3" to "Dark Grove",
            "temperate_grotto" to "Sunken Swamp",

            "tropical_1" to "Tropical Overgrowth",
            "tropical_2" to "Coral Shores",
            "tropical_3" to "Twisted Swamp",
            "tropical_grotto" to "Mirrored Oasis",

            "barren_1" to "Ancient Sands",
            "barren_2" to "Blazing Canyon",
            "barren_3" to "Ashen Wastes",
            "barren_grotto" to "Volcanic Springs",
        )

        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.server == "fishing" && temperatures.any { packet.types.any { type -> type.startsWith(it) } }
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            // find the first type in the list of server types that matches a valid island
            //  we can guarantee we have one, since we checked for it in the check function
            return Fishing(packet.types.first { islandNames.containsKey(it) }, packet.types)
        }
    }
}