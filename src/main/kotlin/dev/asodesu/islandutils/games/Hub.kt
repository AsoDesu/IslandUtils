package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.details
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.timeElapsed
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext
import dev.asodesu.islandutils.api.game.gameManager

class Hub(types: List<String>) : Game("lobby", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("hub")
        details("In the Hub")
        timeElapsed(gameManager.lastGameChange)
    }

    override fun toString() = "Hub"

    companion object : GameContext {
        override val id = "lobby"
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.checkLobby()
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            return Hub(packet.types)
        }
    }
}