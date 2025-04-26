package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.game.state.StateManager
import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.music.resources.handler.SingleDownloadHandler
import dev.asodesu.islandutils.options.MusicOptions
import net.minecraft.resources.ResourceLocation

object TgttosDomeMusic : MusicReplacementModifier {
    override val enableOption = MusicOptions.Modifiers.tgttosDome
    private const val MUSIC_KEY = "music.global.tgttosawaf" // no fans on island :(
    private const val REPLACEMENT_ASSET = "music/to_the_dome"
    private val REPLACEMENT_ASSET_KEY = RemoteResources.key(REPLACEMENT_ASSET)

    override fun replace(server: ResourceLocation): ResourceLocation = REPLACEMENT_ASSET_KEY
    override fun check(server: ResourceLocation): Boolean {
        return server.path == MUSIC_KEY && StateManager.current.mapId == "to_the_dome"
    }

    fun downloadMusic() = SingleDownloadHandler(REPLACEMENT_ASSET)
}