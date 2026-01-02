package dev.asodesu.islandutils.api.extentions

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket

fun nullGameState() = ClientboundMccGameStatePacket("none", "unknown_queue", "unknown_phase", "unknown_stage", -1, -1, "unknown_map", "Unknown Map")

object PhaseType {
    const val LOADING = "LOADING"
    const val WAITING_FOR_PLAYERS = "WAITING_FOR_PLAYERS"
    const val PRE_GAME = "PRE_GAME"
    const val PLAY = "PLAY"
    const val INTERMISSION = "INTERMISSION"
    const val POST_GAME = "POST_GAME"
}