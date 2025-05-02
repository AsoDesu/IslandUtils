package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.islandUtilsFolder
import dev.asodesu.islandutils.api.options.Config

object Options : Config(islandUtilsFolder.resolve("islandutils.json").toFile()) {
    override val entries = listOf(
        MusicOptions,
        DiscordOptions,
        ClassicHitwOptions,
        NotificationOptions,
        MiscOptions
    )
}