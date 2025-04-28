package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.options.option.Option
import dev.asodesu.islandutils.api.sound.info.MutableSoundInfo
import dev.asodesu.islandutils.api.sound.info.SoundInfo

interface MusicModifier {
    val enableOption: Option<Boolean>

    fun modify(info: MutableSoundInfo)
    fun shouldApply(info: SoundInfo): Boolean
}