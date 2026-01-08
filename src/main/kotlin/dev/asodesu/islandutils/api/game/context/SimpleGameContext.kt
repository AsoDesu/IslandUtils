package dev.asodesu.islandutils.api.game.context

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket

abstract class SimpleGameContext(private val islandId: String) : GameContext {
    override val id = islandId

    override fun check(packet: ClientboundMccServerPacket): Boolean {
        return packet.checkGame(islandId)
    }
}