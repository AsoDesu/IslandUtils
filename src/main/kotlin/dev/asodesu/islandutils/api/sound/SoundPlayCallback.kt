package dev.asodesu.islandutils.api.sound

import dev.asodesu.islandutils.api.sound.info.SoundInfo

fun interface SoundPlayCallback {
    fun onSoundPlay(info: SoundInfo, ci: Info)

    interface Info {
        fun replace(info: SoundInfo)
        fun cancel()
    }
}