package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext
import dev.asodesu.islandutils.api.sidebar.sidebar

class HoleInTheWall : Game("hole_in_the_wall") {
    companion object : SimpleGameContext("hole_in_the_wall") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return HoleInTheWall()
        }
    }
}