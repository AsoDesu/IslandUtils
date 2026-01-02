package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.events.sound.info.MutableSoundInfo
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo
import net.minecraft.resources.Identifier

interface MusicReplacementModifier : MusicModifier {
    fun replace(server: Identifier): Identifier?
    fun check(server: Identifier): Boolean

    override fun modify(info: MutableSoundInfo) {
        replace(info.sound)
            ?.let { info.sound = it }
    }

    override fun shouldApply(info: SoundInfo) = check(info.sound)
}