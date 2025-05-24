package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.chest.analysis.ChestAnalysisManager
import dev.asodesu.islandutils.api.game.GameManager
import dev.asodesu.islandutils.api.game.state.StateManager
import dev.asodesu.islandutils.api.music.MusicManager
import dev.asodesu.islandutils.api.music.MusicManager.Track
import dev.asodesu.islandutils.cosmetics.CosmeticUI
import dev.asodesu.islandutils.features.ClassicHitw
import dev.asodesu.islandutils.features.FishingUpgradeHighlight
import dev.asodesu.islandutils.features.FriendsInGame
import dev.asodesu.islandutils.features.RemnantHighlight
import dev.asodesu.islandutils.features.crafting.CraftingChestAnalyser
import dev.asodesu.islandutils.features.crafting.notif.CraftingNotifier
import dev.asodesu.islandutils.features.scavenging.ScavengingTotals
import dev.asodesu.islandutils.games.Fishing
import dev.asodesu.islandutils.games.HoleInTheWall
import dev.asodesu.islandutils.games.Hub
import dev.asodesu.islandutils.games.ParkourWarriorDojo
import dev.asodesu.islandutils.games.Tgttos
import dev.asodesu.islandutils.music.ClassicHitwMusic
import dev.asodesu.islandutils.music.HighQualityMusic
import dev.asodesu.islandutils.music.OldDynaballMusic
import dev.asodesu.islandutils.music.TgttosDomeMusic
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
        ClassicHitw,
        CraftingNotifier
    )

    // the game manager, the order here does matter.
    val gameManager = GameManager(
        Fishing,
        Tgttos,
        HoleInTheWall,
        // ParkourWarriorSurvivor
        ParkourWarriorDojo,
        Hub
    )

    val musicManager = MusicManager(
        knownTracks = listOf(
            Track("music.global.parkour_warrior"),
            Track("music.global.battle_box"),
            Track("music.global.dynaball"),
            Track("music.global.hole_in_the_wall"),
            Track("music.global.sky_battle"),
            Track("music.global.tgttosawaf"),
            Track("music.global.overtime_loop_music"),
            Track("music.global.overtime_intro_music"),
            Track("music.global.gameendmusic", loop = false),
        ),
        modifiers = listOf(
            ClassicHitwMusic,
            HighQualityMusic,
            OldDynaballMusic,
            TgttosDomeMusic,
            TgttosDoubleTime
        )
    )

    val chestAnalysis = ChestAnalysisManager(
        factories = listOf(
            CraftingChestAnalyser.Factory,
            ScavengingTotals.Factory,
            FishingUpgradeHighlight,
            CosmeticUI.Factory
        ),
        analysers = listOf(
            RemnantHighlight
        )
    )
}