package dev.asodesu.islandutils.api.game.events

import dev.asodesu.islandutils.api.game.Game

fun interface GameChangeCallback {
    fun onGameChange(from: Game, to: Game)
}