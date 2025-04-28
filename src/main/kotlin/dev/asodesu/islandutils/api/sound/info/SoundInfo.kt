package dev.asodesu.islandutils.api.sound.info

import java.util.*
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import kotlin.jvm.optionals.getOrNull

interface SoundInfo {
    val sound: ResourceLocation
    val category: SoundSource
    val pitch: Float
    val volume: Float
    val fixedRange: Float?

    fun toSoundEvent() = SoundEvent(sound, Optional.ofNullable(fixedRange))

    fun toImmutable() = ImmutableSoundInfo(this)
    fun toMutable() = MutableSoundInfo(this)

    companion object {
        fun fromPacket(packet: ClientboundSoundPacket) = ImmutableSoundInfo(
            sound = packet.sound.value().location,
            category = packet.source,
            pitch = packet.pitch,
            volume = packet.volume,
            fixedRange = packet.sound.value().fixedRange.getOrNull()
        )
    }
}