package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.sidebarStateLayer
import dev.asodesu.islandutils.api.discord.layers.timeRemaining
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class ParkourWarriorSurvivor(types: List<String>) : Game("parkour_warrior_survivor", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("parkour_warrior_survivor")
        playingDetails("Parkour Warrior: Survivor")
        sidebarStateLayer("LEAP \\[(\\d+\\/\\d+)\\]", "Leap: ")
        timeRemaining()
    }

    override fun toString() = "ParkourWarriorSurvivor"

    companion object : GameContext {
        override val id: String = "parkour_warrior_survivor"
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.checkGame("parkour_warrior", "survivor")
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return ParkourWarriorSurvivor(packet.types)
        }
    }
}