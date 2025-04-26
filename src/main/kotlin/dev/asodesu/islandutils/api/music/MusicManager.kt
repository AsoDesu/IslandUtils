package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.debug
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.options.MusicOptions
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket

class MusicManager(val knownTracks: List<String>, val modifiers: List<MusicModifier>) : Module("MusicManager") {
    private val enabled by MusicOptions.enabled

    private val playingSounds = mutableListOf<MusicSoundInstance>()

    override fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
    }

    fun tick(client: Minecraft) {
        val iterator = playingSounds.iterator()
        iterator.forEach {
            // loop prevention
            val tags = it.handleTags()
            // check if the server triggered a stop on this sound
            if (tags != null && tags.contains(LoopTag.STOPPED)) {
                // check if the server stopped, but didn't restart
                if (!tags.contains(LoopTag.RESTARTED)) {
                    // the server didn't restart, fading...
                    it.fade()
                    iterator.remove()
                    debug("Fading ${it.unmodifiedSound}...")
                }
            }

            if (!minecraft.soundManager.isActive(it)) {
                iterator.remove()
                debug("Music track ${it.unmodifiedSound} stopped, removed")
            }
        }
    }

    // called from mixin, return true to cancel vanilla
    fun handlePacket(packet: ClientboundSoundPacket): Boolean {
        if (!enabled) return false

        val soundLocation = packet.sound.value().location
        if (!soundLocation.path.startsWith("music.")) return false
        if (!knownTracks.contains(soundLocation.path)) return false // ignore music tracks that we don't care about

        val existingSounds = playingSounds.filter { it.unmodifiedSound == soundLocation }
        if (existingSounds.isNotEmpty()) {
            existingSounds.forEach { it.tag(LoopTag.RESTARTED) }
            return true
        }

        val info = SoundInfo(soundLocation, packet.pitch, packet.volume)
        modifiers.forEach {
            if (it.shouldApply(info) && it.enableOption.get()) it.modify(info)
        }

        val instance = MusicSoundInstance(
            sound = info.sound,
            pitch = info.pitch,
            volume = info.volume,
            category = packet.source,
            loop = true,

            unmodifiedSound = soundLocation
        )
        playingSounds += instance
        minecraft.soundManager.play(instance)
        debug("Playing ${soundLocation.path} as a looping track")
        return true
    }

    // called from mixin
    fun handlePacket(packet: ClientboundStopSoundPacket): Boolean {
        // functionality similar to that of vanilla stopping behaviour (defined in SoundEngine#stop)
        val source = packet.source
        val name = packet.name
        return if (source != null) {
            // source is set, name may or may not be set
            // fade all sounds on this source, or on this source with this name
            playingSounds.tagIf(LoopTag.STOPPED) { it.source == source && (name == null || it.unmodifiedSound == name) }
        } else if (name == null) {
            // source and name are null, fade all
            playingSounds.tagIf(LoopTag.STOPPED) { true }
        } else {
            // source is null, name is set, fade all with this name
            playingSounds.tagIf(LoopTag.STOPPED) { it.unmodifiedSound == name }
        }
    }

    private fun MutableList<MusicSoundInstance>.tagIf(tag: LoopTag, func: (MusicSoundInstance) -> Boolean): Boolean {
        return this.any {
            if (func(it)) {
                it.tag(tag)
                true
            } else false
        }
    }
}