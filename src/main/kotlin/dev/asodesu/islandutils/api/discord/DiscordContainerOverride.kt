package dev.asodesu.islandutils.api.discord

import dev.asodesu.islandutils.api.discord.container.DiscordContainer

class DiscordContainerOverride(
    val reason: String,
    val priority: Int,
    val check: () -> Boolean,
    val create: () -> DiscordContainer
)