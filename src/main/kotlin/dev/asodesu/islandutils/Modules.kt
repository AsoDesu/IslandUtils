package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.game.GameManager
import dev.asodesu.islandutils.api.game.state.StateManager
import dev.asodesu.islandutils.api.music.MusicManager
import dev.asodesu.islandutils.features.ClassicHitw
import dev.asodesu.islandutils.features.FriendsInGame
import dev.asodesu.islandutils.games.Hub
import dev.asodesu.islandutils.games.ParkourWarriorDojo
import dev.asodesu.islandutils.games.Tgttos
import dev.asodesu.islandutils.music.ClassicHitwMusic
import dev.asodesu.islandutils.music.HighQualityMusic
import dev.asodesu.islandutils.music.OldDynaballMusic
import dev.asodesu.islandutils.music.TgttosDoubleTime

/**
 * The modules used which require initialisation.
 * Every field in this class of type `Module` will be initialised
 * as a module at startup.
 */
object Modules {
    // modules which are defined as objects
    val objects = listOf(
        StateManager,
        FriendsInGame,
        ClassicHitw
    )

    // the game manager, the order here does matter.
    val gameManager = GameManager(
        Hub,
        Tgttos,
        // ParkourWarriorSurvivor
        ParkourWarriorDojo
    )

    val musicManager = MusicManager(
        knownTracks = listOf(
            "music.global.parkour_warrior",
            "music.global.battle_box",
            "music.global.dynaball",
            "music.global.hole_in_the_wall",
            "music.global.sky_battle",
            "music.global.tgttosawaf",
            "music.global.overtime_loop_music"
        ),
        modifiers = listOf(
            HighQualityMusic,
            OldDynaballMusic,
            ClassicHitwMusic,
            TgttosDoubleTime
        )
    )
}