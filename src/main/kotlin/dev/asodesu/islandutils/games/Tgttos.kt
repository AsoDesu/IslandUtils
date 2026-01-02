package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sidebar.sidebar
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext

class Tgttos : Game("tgttos") {
    override val hasTeamChat = false
    val modifier by sidebar("MODIFIER: ([\\w ]+)")

    companion object : SimpleGameContext("tgttos") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return Tgttos()
        }
    }
}