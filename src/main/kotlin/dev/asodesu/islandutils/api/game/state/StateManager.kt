package dev.asodesu.islandutils.api.game.state

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.MccPackets
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.extentions.nullGameState
import dev.asodesu.islandutils.api.game.GameEvents
import dev.asodesu.islandutils.api.modules.Module

object StateManager : Module("StateManager") {
    var current = nullGameState()

    override fun init() {
        MccPackets.CLIENTBOUND_MCC_GAME_STATE.addListener(this, ClientboundMccGameStatePacket::class.java) { _, packet, _ ->
            this.onMccGameState(packet)
        }
    }

    private fun onMccGameState(packet: ClientboundMccGameStatePacket) {
        debug("Game State: $packet")
        GameEvents.GAME_STATE_UPDATE.invoker().onGameStateUpdate(current, packet)
        current = packet
    }
}