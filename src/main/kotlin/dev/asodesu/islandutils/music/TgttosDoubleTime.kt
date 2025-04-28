package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.game.activeGame
import dev.asodesu.islandutils.api.music.MusicModifier
import dev.asodesu.islandutils.api.sound.info.MutableSoundInfo
import dev.asodesu.islandutils.api.sound.info.SoundInfo
import dev.asodesu.islandutils.games.Tgttos
import dev.asodesu.islandutils.options.MusicOptions

object TgttosDoubleTime : MusicModifier {
    override val enableOption = MusicOptions.Modifiers.tgttosDoubleTime

    override fun modify(info: MutableSoundInfo) {
        info.pitch = 1.2f
    }

    override fun shouldApply(info: SoundInfo): Boolean {
        // only apply during TGTTOS, when DOUBLE TIME is active
        val tgttos = activeGame as? Tgttos ?: return false
        return tgttos.modifier?.startsWith("DOUBLE TIME", ignoreCase = true) ?: false
    }
}