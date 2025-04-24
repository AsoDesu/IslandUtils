package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.configDir
import dev.asodesu.islandutils.api.options.Config

object Options : Config(configDir.resolve("islandutils.json").toFile()) {
    override val entries = listOf(
        MusicOptions,
        DiscordOptions,
        MiscOptions
    )
}