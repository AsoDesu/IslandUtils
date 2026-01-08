package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.extentions.isOnline
import dev.asodesu.islandutils.api.game.gameManager
import dev.asodesu.islandutils.api.options.ConfigSection
import dev.asodesu.islandutils.api.options.onChange
import dev.asodesu.islandutils.api.options.onDisabled
import dev.asodesu.islandutils.api.options.onEnable

object DiscordOptions : ConfigSection("section.discord") {
    val enabled = toggle("discord_enabled", def = true, desc = true)
        .onEnable { if (isOnline) gameManager.pushDiscord() }
        .onDisabled { DiscordManager.resetContainer() }

    val showPlace = toggle("discord_show_place", def = true, desc = true)
        .onChange { if (isOnline) gameManager.pushDiscord() }
}