package dev.asodesu.islandutils.api.game

import org.slf4j.LoggerFactory

/**
 * A game, for managing game specific modules, music and discord presence. Initialised
 * when the player enters the game, and a corresponding MCC Server packet is received.
 *
 * @param id An identifier for this game (only used within mod,
 *  doesn't need to match island counterpart)
 */
abstract class Game(val id: String) {
    protected val logger = LoggerFactory.getLogger("IU-Game'$id'")

    /**
     * Called when the player changes servers away from this game.
     */
    open fun unregister() {}
}