package dev.asodesu.islandutils.api.game

import dev.asodesu.islandutils.api.events.arrayBackedEvent
import dev.asodesu.islandutils.api.game.events.GameChangeCallback
import dev.asodesu.islandutils.api.game.events.GameStateUpdateCallback
import dev.asodesu.islandutils.api.game.events.ServerUpdateCallback

object GameEvents {
    /**
     * Called when one game is unregistered and another takes it's place
     */
    val GAME_CHANGE = arrayBackedEvent { callbacks ->
        GameChangeCallback { from, to ->
            callbacks.forEach { it.onGameChange(from, to) }
        }
    }

    /**
     * When we receive an MCC Server Packet, BEFORE any game initialisation takes place.
     */
    val SERVER_UPDATE = arrayBackedEvent { callbacks ->
        ServerUpdateCallback { packet ->
            callbacks.forEach { it.onServerUpdate(packet) }
        }
    }

    /**
     * When we receive an MCC Game State Update Packet.
     */
    val GAME_STATE_UPDATE = arrayBackedEvent { callbacks ->
        GameStateUpdateCallback { from, to ->
            callbacks.forEach { it.onGameStateUpdate(from, to) }
        }
    }
}