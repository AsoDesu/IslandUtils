package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playersRemainingState
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.timeRemaining
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class HoleInTheWall(types: List<String>) : Game("hole_in_the_wall", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("hitw")
        playingDetails("Hole in the Wall")
        playersRemainingState()
        timeRemaining()
    }

    override fun toString() = "HoleInTheWall"

    companion object : SimpleGameContext("hole_in_the_wall") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return HoleInTheWall(packet.types)
        }
    }
}