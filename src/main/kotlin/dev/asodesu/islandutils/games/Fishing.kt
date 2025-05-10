package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class Fishing(val island: String) : Game("lobby") {

    companion object : GameContext {
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
            return packet.serverType == "lobby" && temperatures.any { packet.subType.startsWith("${it}_") }
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return Fishing(packet.subType)
        }
    }
}