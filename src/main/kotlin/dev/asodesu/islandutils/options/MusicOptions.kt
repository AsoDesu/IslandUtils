package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.options.ConfigSection
import dev.asodesu.islandutils.api.options.download.withDownloadJob
import dev.asodesu.islandutils.music.HighQualityMusic
import dev.asodesu.islandutils.music.OldDynaballMusic
import dev.asodesu.islandutils.music.TgttosDomeMusic

object MusicOptions : ConfigSection("section.music") {
    val enabled = toggle("music_enabled", def = true, desc = true)

    object Modifiers : ConfigGroup("section.music.modifiers") {
        val highQualityMusic = toggle("music_highq", def = true, desc = true).withDownloadJob(HighQualityMusic.DOWNLOAD_JOB)
        val oldDynaball = toggle("music_olddynaball", def = false, desc = true).withDownloadJob(OldDynaballMusic.DOWNLOAD_JOB)
        val tgttosDoubleTime = toggle("music_tgttosdouble", def = true, desc = true)
        val tgttosDome = toggle("music_tgttosdome", def = true, desc = true).withDownloadJob(TgttosDomeMusic.DOWNLOAD_JOB)
    }

    init {
        group(Modifiers)
    }
}