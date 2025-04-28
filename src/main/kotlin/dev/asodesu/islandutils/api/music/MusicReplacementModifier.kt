package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.sound.info.MutableSoundInfo
import dev.asodesu.islandutils.api.sound.info.SoundInfo
import net.minecraft.resources.ResourceLocation

interface MusicReplacementModifier : MusicModifier {
    fun replace(server: ResourceLocation): ResourceLocation?
    fun check(server: ResourceLocation): Boolean

    override fun modify(info: MutableSoundInfo) {
        replace(info.sound)
            ?.let { info.sound = it }
    }

    override fun shouldApply(info: SoundInfo) = check(info.sound)
}