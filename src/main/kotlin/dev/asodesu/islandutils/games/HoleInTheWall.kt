package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class HoleInTheWall : Game("hole_in_the_wall") {
    override val hasTeamChat = false

    companion object : SimpleGameContext("hole_in_the_wall") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return HoleInTheWall()
        }
    }
}