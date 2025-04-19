package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class Tgttos : Game("tgttos") {
    companion object : SimpleGameContext("tgttos") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return Tgttos()
        }
    }
}