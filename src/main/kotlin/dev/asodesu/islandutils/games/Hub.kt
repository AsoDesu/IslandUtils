package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext

class Hub : Game("lobby") {
    override val hasTeamChat = false

    companion object : GameContext {
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.serverType == "lobby"
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return Hub()
        }
    }
}