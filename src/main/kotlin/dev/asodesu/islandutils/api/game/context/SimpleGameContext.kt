package dev.asodesu.islandutils.api.game.context

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket

abstract class SimpleGameContext(private val islandId: String) : GameContext {
    override fun check(packet: ClientboundMccServerPacket): Boolean {
        return packet.associatedGame == islandId
    }
}