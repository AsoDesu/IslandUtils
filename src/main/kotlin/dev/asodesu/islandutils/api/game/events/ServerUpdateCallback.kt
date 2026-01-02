package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket

fun interface ServerUpdateCallback {
    fun onServerUpdate(packet: ClientboundMccServerPacket)
}