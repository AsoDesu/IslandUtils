package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.options.ConfigSection

object MusicOptions : ConfigSection("section.music") {
    val enabled = toggle("music_enabled", def = true, desc = true)

    object Modifiers : ConfigGroup("section.music.modifiers") {
        val tgttosDoubleTime = toggle("music_tgttosdouble", def = true, desc = true)
    }
}