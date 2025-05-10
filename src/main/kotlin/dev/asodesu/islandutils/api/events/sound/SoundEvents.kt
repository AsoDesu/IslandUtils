package dev.asodesu.islandutils.api.events.sound

import dev.asodesu.islandutils.api.events.MutableListEvent

object SoundEvents {
    @JvmStatic
    val SOUND_PLAY = MutableListEvent<SoundPlayCallback> { callbacks ->
        SoundPlayCallback { info, ci ->
            callbacks.forEach { it.onSoundPlay(info, ci) }
        }
    }

    @JvmStatic
    val SOUND_STOP = MutableListEvent<SoundStopCallback> { callbacks ->
        SoundStopCallback { info, ci ->
            callbacks.forEach { it.onSoundStop(info, ci) }
        }
    }
}