package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicReplacementModifier
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.features.ClassicHitw
import dev.asodesu.islandutils.options.ClassicHitwOptions
import net.minecraft.resources.Identifier

object ClassicHitwMusic : MusicReplacementModifier {
    override val enableOption = ClassicHitwOptions.music
    private const val MUSIC_KEY = "music.global.hole_in_the_wall"
    private val REPLACEMENT_ASSET_KEY = RemoteResources.key(ClassicHitw.MUSIC_ASSET)

    override fun replace(server: Identifier): Identifier = REPLACEMENT_ASSET_KEY
    override fun check(server: Identifier) = server.path == MUSIC_KEY
    override fun downloadJob() = ClassicHitw.MUSIC_DOWNLOAD
}