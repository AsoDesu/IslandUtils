package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.roundState
import dev.asodesu.islandutils.api.discord.layers.timeRemaining
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class BattleBox(types: List<String>) : Game("battle_box", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("battle_box")
        playingDetails("Battle Box")
        roundState()
        timeRemaining()
    }

    override fun toString() = "BattleBox"

    companion object : SimpleGameContext("battle_box") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return BattleBox(packet.types)
        }
    }
}