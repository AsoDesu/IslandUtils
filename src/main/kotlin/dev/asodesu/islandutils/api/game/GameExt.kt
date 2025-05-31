package dev.asodesu.islandutils.api.game

import dev.asodesu.islandutils.Modules
import dev.asodesu.islandutils.games.Fishing
import dev.asodesu.islandutils.games.Hub

/**
 * The game manager
 */
val gameManager: GameManager
    get() = Modules.gameManager

/**
 * The current game the player is in
 */
val activeGame: Game
    get() = gameManager.active

/**
 * If the player is in the lobby, by checking if the
 * current active game is `Hub`
 */
val inLobby: Boolean
    get() = activeGame is Hub

/**
 * If the player is in the lobby or fishing, by checking if the
 * current active game is `Hub` or `Fishing`
 */
val inLobbyOrFishing: Boolean
    get() = activeGame is Hub || activeGame is Fishing

/**
 * If the player is in game, by checking if the current
 * active game is not `Hub`
 */
val inGame: Boolean
    get() = !inLobby