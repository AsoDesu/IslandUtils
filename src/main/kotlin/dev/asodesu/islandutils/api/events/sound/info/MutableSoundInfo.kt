package dev.asodesu.islandutils.api.events.sound.info

import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundSource

class MutableSoundInfo(
    override var sound: Identifier,
    override var category: SoundSource = SoundSource.MASTER,
    override var pitch: Float,
    override var volume: Float,
    override var fixedRange: Float? = null,
    override var loop: Boolean = false
) : SoundInfo {
    constructor(from: SoundInfo) : this(from.sound, from.category, from.pitch, from.volume, from.fixedRange, from.loop)
}