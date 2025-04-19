package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.game.GameManager
import dev.asodesu.islandutils.api.game.state.StateManager
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.games.Hub
import dev.asodesu.islandutils.games.ParkourWarriorDojo
import dev.asodesu.islandutils.games.Tgttos

/**
 * The modules used which require initialisation.
 * Every field in this class of type `Module` will be initialised
 * as a module at startup.
 */
object Modules {
    // modules which are defined as objects
    val objects = listOf<Module>(
        StateManager
    )

    // the game manager, the order here does matter.
    val gameManager = GameManager(
        Hub,
        Tgttos,
        // ParkourWarriorSurvivor
        ParkourWarriorDojo
    )
}