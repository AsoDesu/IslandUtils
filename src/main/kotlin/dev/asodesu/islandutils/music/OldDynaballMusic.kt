package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.DownloadJob
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.Identifier

object OldDynaballMusic : MusicReplacementModifier {
    override val enableOption by lazy { MusicOptions.Modifiers.oldDynaball } // lazyload because options access download
    private const val MUSIC_KEY = "music.global.dynaball"
    private const val REPLACEMENT_ASSET = "old_dynaball/music"
    val DOWNLOAD_JOB = DownloadJob.single(REPLACEMENT_ASSET)
    private val REPLACEMENT_ASSET_KEY = RemoteResources.key(REPLACEMENT_ASSET)

    override fun replace(server: Identifier): Identifier = REPLACEMENT_ASSET_KEY
    override fun check(server: Identifier) = server.path == MUSIC_KEY
    override fun downloadJob() = DOWNLOAD_JOB
}