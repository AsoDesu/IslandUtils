package dev.asodesu.islandutils.api.extentions

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket

fun nullGameState() = ClientboundMccGameStatePacket("LOADING", "unknown", 0, 0, "unknown", "Unknown")

object PhaseType {
    const val LOADING = "LOADING"
    const val WAITING_FOR_PLAYERS = "WAITING_FOR_PLAYERS"
    const val PRE_GAME = "PRE_GAME"
    const val PLAY = "PLAY"
    const val INTERMISSION = "INTERMISSION"
    const val POST_GAME = "POST_GAME"
}