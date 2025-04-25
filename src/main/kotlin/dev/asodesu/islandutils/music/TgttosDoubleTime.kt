package dev.asodesu.islandutils.music

import dev.asodesu.islandutils.api.music.MusicModifier
import dev.asodesu.islandutils.api.music.SoundInfo
import dev.asodesu.islandutils.api.options.option.Option
import dev.asodesu.islandutils.options.MusicOptions

object TgttosDoubleTime : MusicModifier {
    override val enableOption = MusicOptions.Modifiers.tgttosDoubleTime

    override fun modify(info: SoundInfo) {
        info.pitch = 1.5f
    }

    override fun shouldApply(info: SoundInfo): Boolean {
        return true
    }
}