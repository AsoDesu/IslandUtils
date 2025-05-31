package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.extentions.islandUtilsFolder
import dev.asodesu.islandutils.api.options.Config

object Options : Config(islandUtilsFolder.resolve("islandutils.json").toFile()) {
    override val entries = listOf(
        MusicOptions,
        CosmeticOptions,
        DiscordOptions,
        ClassicHitwOptions,
        NotificationOptions,
        MiscOptions
    )
}