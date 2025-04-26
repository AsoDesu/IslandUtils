package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigSection
import dev.asodesu.islandutils.api.options.onEnable
import dev.asodesu.islandutils.features.ClassicHitw
import dev.asodesu.islandutils.music.ClassicHitwMusic

object ClassicHitwOptions : ConfigSection("section.classic_hitw") {
    val annoucer = toggle("classic_hitw_annoucer", def = false, desc = true)
        .onEnable { ClassicHitw.downloadAnnoucer() }
    val music = toggle("classic_hitw_music", def = false, desc = true)
        .onEnable { ClassicHitwMusic.downloadMusic() }
}