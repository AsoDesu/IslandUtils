package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket

fun interface ServerUpdateCallback {
    fun onServerUpdate(packet: ClientboundMccServerPacket)
}