package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playersRemainingState
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.timeRemaining
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class RocketSpleefRush(types: List<String>) : Game("rocket_spleef", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("rocket_spleef_rush")
        playingDetails("Rocket Spleef Rush")
        playersRemainingState()
        timeRemaining()
    }

    override fun toString() = "RocketSpleefRush"

    companion object : SimpleGameContext("rocket_spleef") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return RocketSpleefRush(packet.types)
        }
    }
}