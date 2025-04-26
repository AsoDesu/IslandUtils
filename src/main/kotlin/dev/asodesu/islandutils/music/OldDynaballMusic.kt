package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.SingleDownloadHandler
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.ResourceLocation

object OldDynaballMusic : MusicReplacementModifier {
    override val enableOption = MusicOptions.Modifiers.oldDynaball
    private const val MUSIC_KEY = "music.global.dynaball"
    private const val REPLACEMENT_ASSET = "music/dynaball"
    private val REPLACEMENT_ASSET_KEY = RemoteResources.key(REPLACEMENT_ASSET)

    override fun replace(server: ResourceLocation): ResourceLocation = REPLACEMENT_ASSET_KEY
    override fun check(server: ResourceLocation) = server.path == MUSIC_KEY

    fun downloadMusic() = SingleDownloadHandler(REPLACEMENT_ASSET)
}