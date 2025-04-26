package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.sidebar.SidebarEvents
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext
import dev.asodesu.islandutils.api.sidebar.SidebarLineUpdate
import dev.asodesu.islandutils.api.sidebar.sidebar

class Tgttos : Game("tgttos") {
    val modifier by sidebar("MODIFIER: ([\\w ]+)")

    companion object : SimpleGameContext("tgttos") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return Tgttos()
        }
    }
}