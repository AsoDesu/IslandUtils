package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sidebar.sidebar
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class ParkourWarriorDojo : Game("parkour_warrior_dojo") {
    override val hasTeamChat = false
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