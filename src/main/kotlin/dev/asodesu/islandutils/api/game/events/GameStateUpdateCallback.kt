package dev.asodesu.islandutils.api.game.events

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import dev.asodesu.islandutils.api.game.Game

fun interface GameStateUpdateCallback {
    fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket)
}