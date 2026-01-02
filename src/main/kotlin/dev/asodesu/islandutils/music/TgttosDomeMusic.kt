package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.game.state.StateManager
import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.DownloadJob
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.Identifier

object TgttosDomeMusic : MusicReplacementModifier {
    override val enableOption by lazy { MusicOptions.Modifiers.tgttosDome } // lazyload because options access download

    private const val REPLACEMENT_ASSET = "music/to_the_dome"
    private const val MUSIC_KEY = "music.global.tgttosawaf" // no fans on island :(
    private val REPLACEMENT_ASSET_KEY = RemoteResources.key(REPLACEMENT_ASSET)
    val DOWNLOAD_JOB = DownloadJob.single(REPLACEMENT_ASSET)

    override fun replace(server: Identifier): Identifier = REPLACEMENT_ASSET_KEY
    override fun check(server: Identifier): Boolean {
        return server.path == MUSIC_KEY && StateManager.current.mapId == "to_the_dome"
    }
    override fun downloadJob() = DOWNLOAD_JOB
}