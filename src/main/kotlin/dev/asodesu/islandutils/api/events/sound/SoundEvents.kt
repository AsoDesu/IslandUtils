package dev.asodesu.islandutils.api.events.sound

import dev.asodesu.islandutils.api.events.arrayBackedEvent

object SoundEvents {
    @JvmStatic
    val SOUND_PLAY = arrayBackedEvent { callbacks ->
        SoundPlayCallback { info, ci ->
            callbacks.forEach { it.onSoundPlay(info, ci) }
        }
    }

    @JvmStatic
    val SOUND_STOP = arrayBackedEvent { callbacks ->
        SoundStopCallback { info, ci ->
            callbacks.forEach { it.onSoundStop(info, ci) }
        }
    }
}