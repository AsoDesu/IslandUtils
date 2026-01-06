package dev.asodesu.islandutils.api.game

object EmptyGame : Game("none", emptyList()) {
    override val hasTeamChat = false
}