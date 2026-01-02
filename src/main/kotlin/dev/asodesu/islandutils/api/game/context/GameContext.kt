package dev.asodesu.islandutils.api.game.context

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.game.Game

/**
 * The context for creating `Game` objects based on incoming MCC Server packets
 */
interface GameContext {

    /**
     * Checks if this game should be started by this packet.
     *
     * @param packet The incoming MCC Server packet
     * @return true if this game is defined in this packet
     */
    fun check(packet: ClientboundMccServerPacket): Boolean

    /**
     * Creates a new instance of this game.
     *
     * @param packet The incoming MCC Server packet
     * @return A new instance of this game
     */
    fun create(packet: ClientboundMccServerPacket): Game
}