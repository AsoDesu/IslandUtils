package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playersRemainingState
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.timeRemaining
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class SkyBattle(types: List<String>) : Game("sky_battle", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("sky_battle")
        playingDetails("Sky Battle")
        playersRemainingState()
        timeRemaining()
    }

    override fun toString() = "SkyBattle"

    companion object : SimpleGameContext("sky_battle") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return SkyBattle(packet.types)
        }
    }
}