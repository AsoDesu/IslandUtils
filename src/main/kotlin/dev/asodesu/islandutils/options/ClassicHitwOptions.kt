package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigSection
import dev.asodesu.islandutils.api.options.download.withDownloadJob
import dev.asodesu.islandutils.features.ClassicHitw

object ClassicHitwOptions : ConfigSection("section.classic_hitw") {
    val annoucer = toggle("classic_hitw_annoucer", def = false, desc = true).withDownloadJob(ClassicHitw.ANNOUNCER_DOWNLOAD)
    val music = toggle("classic_hitw_music", def = false, desc = true).withDownloadJob(ClassicHitw.MUSIC_DOWNLOAD)
}