package dev.asodesu.islandutils.api.game

import dev.asodesu.islandutils.api.discord.container.DiscordContainer

object EmptyGame : Game("none", emptyList()) {
    override val hasTeamChat = false
    override val discord = DiscordContainer.default
}