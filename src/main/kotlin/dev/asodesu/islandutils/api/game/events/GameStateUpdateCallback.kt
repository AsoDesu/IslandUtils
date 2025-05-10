package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket

fun interface GameStateUpdateCallback {
    fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket)
}