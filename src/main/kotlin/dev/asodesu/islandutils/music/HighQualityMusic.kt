package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.MultiDownloadHandler
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.ResourceLocation

object HighQualityMusic : MusicReplacementModifier {
    override val enableOption = MusicOptions.Modifiers.highQualityMusic
    private val musicOverrides = mutableMapOf(
        "music.global.parkour_warrior" to "music/parkour_warrior",
        "music.global.battle_box" to "music/battle_box",
        "music.global.hole_in_the_wall" to "music/hole_in_the_wall",
        "music.global.sky_battle" to "music/sky_battle",
        "music.global.tgttosawaf" to "music/tgttos",
        "music.global.rocket_spleef" to "music/tgttos",
    )

    override fun replace(server: ResourceLocation) = musicOverrides[server.path]?.let { RemoteResources.key(it) }
    override fun check(server: ResourceLocation) = musicOverrides.containsKey(server.path)

    fun downloadMusic() = MultiDownloadHandler(musicOverrides.values.toList())
}