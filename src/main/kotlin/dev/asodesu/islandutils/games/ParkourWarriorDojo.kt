package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sidebar.sidebar
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class ParkourWarriorDojo : Game("parkour_warrior_dojo") {
    override val hasTeamChat = false
    val course by sidebar("COURSE: (.+)")

    companion object : GameContext {
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.types.contains("parkour_warrior")
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return ParkourWarriorDojo()
        }
    }
}