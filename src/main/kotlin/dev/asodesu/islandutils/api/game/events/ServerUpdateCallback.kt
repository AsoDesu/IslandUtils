package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game

fun interface ServerUpdateCallback {
    fun onServerUpdate(packet: ClientboundMccServerPacket)
}