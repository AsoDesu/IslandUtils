package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.debug
import dev.asodesu.islandutils.api.ticks
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MusicSoundInstance(
    sound: ResourceLocation,
    category: SoundSource,
    loop: Boolean = false,
    pitch: Float = 1f,
    volume: Float = 1f,

    val unmodifiedSound: ResourceLocation = sound
) : AbstractTickableSoundInstance(SoundEvent.createVariableRangeEvent(sound), category, randomSource) {
    companion object {
        private val randomSource = RandomSource.create()
    }

    private var loopTags = mutableListOf<LoopTag>()
    private var fade: FadeTask? = null

    init {
        this.pitch = pitch
        this.volume = volume
        this.attenuation = SoundInstance.Attenuation.NONE
        this.looping = loop
    }

    override fun tick() {
        this.fade?.let { task ->
            this.volume = task.tick()
            if (task.isFinished) this.fade = null
            if (this.volume <= 0f) this.stop()
        }
    }

    /**
     * Returns and clears all loop tags on this instance
     *
     * @return all the loop tags
     */
    fun handleTags(): List<LoopTag>? {
        if (loopTags.isEmpty()) return null
        return loopTags.toList()
            .also { loopTags.clear() }
    }

    /**
     * Adds a new LoopTag to this instance
     */
    fun tag(loopTag: LoopTag) {
        loopTags.add(loopTag)
        debug("Tagged $unmodifiedSound with $loopTag")
    }

    fun fade(duration: Duration = 0.5.seconds, to: Float = 0f) {
        this.fade = FadeTask(this.volume, to, duration.ticks)
    }

    class FadeTask(val start: Float, val end: Float, val duration: Int) {
        private var ticks = 0
        val isFinished get() = ticks >= duration
        fun tick() = Mth.lerp(++ticks / duration.toFloat(), start, end)
    }

}