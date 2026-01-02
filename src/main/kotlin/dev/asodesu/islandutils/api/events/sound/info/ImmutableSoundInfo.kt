package dev.asodesu.islandutils.api.events.sound.info

import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundSource

class ImmutableSoundInfo(
    override val sound: Identifier,
    override val category: SoundSource = SoundSource.MASTER,
    override val pitch: Float,
    override val volume: Float,
    override val fixedRange: Float? = null,
    override val loop: Boolean = false
) : SoundInfo {
    constructor(from: SoundInfo) : this(from.sound, from.category, from.pitch, from.volume, from.fixedRange, from.loop)
}