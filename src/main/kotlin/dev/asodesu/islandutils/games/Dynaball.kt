package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playersRemainingState
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.timeElapsed
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class Dynaball(types: List<String>) : Game("dynaball", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("dynaball")
        playingDetails("Dynaball")
        playersRemainingState()
        timeElapsed()
    }

    override fun toString() = "Dynaball"

    companion object : SimpleGameContext("dynaball") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return Dynaball(packet.types)
        }
    }
}