package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class Hub(types: List<String>) : Game("lobby", types) {
    override val hasTeamChat = false

    override fun toString() = "Hub"

    companion object : GameContext {
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.checkLobby()
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return Hub(packet.types)
        }
    }
}