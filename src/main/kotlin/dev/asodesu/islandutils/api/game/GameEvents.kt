package dev.asodesu.islandutils.api.game

import dev.asodesu.islandutils.api.events.MutableListEvent
import dev.asodesu.islandutils.api.game.events.GameChangeCallback
import dev.asodesu.islandutils.api.game.events.GameStateUpdateCallback
import dev.asodesu.islandutils.api.game.events.ServerUpdateCallback
import dev.asodesu.islandutils.api.sidebar.SidebarLineUpdate
import net.fabricmc.fabric.api.event.EventFactory

object GameEvents {
    /**
     * Called when one game is unregistered and another takes it's place
     */
    val GAME_CHANGE = MutableListEvent<GameChangeCallback> { callbacks ->
        GameChangeCallback { from, to ->
            callbacks.forEach { it.onGameChange(from, to) }
        }
    }

    /**
     * When we receive an MCC Server Packet, BEFORE any game initialisation takes place.
     */
    val SERVER_UPDATE = MutableListEvent<ServerUpdateCallback> { callbacks ->
        ServerUpdateCallback { packet ->
            callbacks.forEach { it.onServerUpdate(packet) }
        }
    }

    /**
     * When we receive an MCC Game State Update Packet.
     */
    val GAME_STATE_UPDATE = MutableListEvent<GameStateUpdateCallback> { callbacks ->
        GameStateUpdateCallback { from, to ->
            callbacks.forEach { it.onGameStateUpdate(from, to) }
        }
    }
}