package dev.asodesu.islandutils.api.events.sound

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource

fun interface SoundStopCallback {
    fun onSoundStop(info: StopInfo, ci: Info)

    data class StopInfo(val name: ResourceLocation?, val source: SoundSource?)
    interface Info {
        fun cancel()
    }
}