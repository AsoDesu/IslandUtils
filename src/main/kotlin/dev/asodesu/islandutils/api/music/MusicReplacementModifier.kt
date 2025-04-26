package dev.asodesu.islandutils.api.music

import net.minecraft.resources.ResourceLocation

interface MusicReplacementModifier : MusicModifier {
    fun replace(server: ResourceLocation): ResourceLocation?
    fun check(server: ResourceLocation): Boolean

    override fun modify(info: SoundInfo) {
        replace(info.sound)
            ?.let { info.sound = it }
    }

    override fun shouldApply(info: SoundInfo) = check(info.sound)
}