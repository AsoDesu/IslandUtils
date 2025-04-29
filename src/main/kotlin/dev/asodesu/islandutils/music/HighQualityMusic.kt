package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.DownloadJob
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.ResourceLocation

object HighQualityMusic : MusicReplacementModifier {
    override val enableOption by lazy { MusicOptions.Modifiers.highQualityMusic } // lazyload because options access download
    private val musicOverrides = mutableMapOf(
        "music.global.parkour_warrior" to "music/parkour_warrior",
        "music.global.battle_box" to "music/battle_box",
        "music.global.hole_in_the_wall" to "music/hole_in_the_wall",
        "music.global.sky_battle" to "music/sky_battle",
        "music.global.tgttosawaf" to "music/tgttos",
        "music.global.rocket_spleef" to "music/tgttos",
    )
    val DOWNLOAD_JOB = DownloadJob.multi(musicOverrides.values)

    override fun replace(server: ResourceLocation) = musicOverrides[server.path]?.let { RemoteResources.key(it) }
    override fun check(server: ResourceLocation) = musicOverrides.containsKey(server.path)
    override fun downloadJob() = DOWNLOAD_JOB
}