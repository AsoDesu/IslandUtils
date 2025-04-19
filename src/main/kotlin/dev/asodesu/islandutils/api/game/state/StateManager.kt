package dev.asodesu.islandutils.api.game.state

import com.noxcrew.noxesium.network.NoxesiumPackets
import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import dev.asodesu.islandutils.api.game.GameEvents
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.nullGameState

object StateManager : Module("StateManager") {
    var current = nullGameState()

    override fun init() {
        NoxesiumPackets.CLIENT_MCC_GAME_STATE.addListener(this) { _, packet, _ ->
            this.onMccGameState(packet)
        }
    }

    private fun onMccGameState(packet: ClientboundMccGameStatePacket) {
        GameEvents.GAME_STATE_UPDATE.invoker().onGameStateUpdate(current, packet)
        current = packet
    }
}