package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket

fun interface GameStateUpdateCallback {
    fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket)
}