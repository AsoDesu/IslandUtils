package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.options.option.Option

interface MusicModifier {
    val enableOption: Option<Boolean>

    fun modify(info: SoundInfo)
    fun shouldApply(info: SoundInfo): Boolean
}