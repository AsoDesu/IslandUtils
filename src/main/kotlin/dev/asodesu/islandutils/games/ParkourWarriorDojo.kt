package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext
import dev.asodesu.islandutils.api.sidebar.sidebar

class ParkourWarriorDojo : Game("parkour_warrior_dojo") {
    val course by sidebar("COURSE: (.+)")

    companion object : GameContext {
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.associatedGame == "parkour_warrior"
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return ParkourWarriorDojo()
        }
    }
}