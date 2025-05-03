package dev.asodesu.islandutils.api.music

import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.events.sound.SoundEvents
import dev.asodesu.islandutils.api.events.sound.SoundPlayCallback
import dev.asodesu.islandutils.api.events.sound.SoundStopCallback
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo
import dev.asodesu.islandutils.options.MusicOptions
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft

class MusicManager(val knownTracks: List<String>, val modifiers: List<MusicModifier>) : Module("MusicManager") {
    private val enabled by MusicOptions.enabled

    private val playingSounds = mutableListOf<MusicSoundInstance>()

    override fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
        SoundEvents.SOUND_PLAY.addListener(::handleSoundPlay)
        SoundEvents.SOUND_STOP.addListener(::handleSoundStop)

        modifiers.forEach {
            val download = it.downloadJob() ?: return@forEach
            if (it.enableOption.get()) download.start()
        }
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

    fun handleSoundPlay(info: SoundInfo, ci: SoundPlayCallback.Info) {
        if (!enabled) return
        if (info.sound.namespace != "mcc") return

        val soundLocation = info.sound
        if (!soundLocation.path.startsWith("music.")) return
        if (!knownTracks.contains(soundLocation.path)) return // ignore music tracks that we don't care about

        val existingSounds = playingSounds.filter { it.unmodifiedSound == soundLocation }
        if (existingSounds.isNotEmpty()) {
            existingSounds.forEach { it.tag(LoopTag.RESTARTED) }
            return ci.cancel()
        }

        val newSound = info.toMutable()
        modifiers.forEach {
            if (it.shouldApply(info) && it.enableOption.get()) it.modify(newSound)
        }

        val instance = MusicSoundInstance(
            sound = newSound.sound,
            pitch = newSound.pitch,
            volume = newSound.volume,
            category = newSound.category,
            loop = true,

            unmodifiedSound = soundLocation
        )
        playingSounds += instance
        minecraft.soundManager.play(instance)
        debug("Playing ${soundLocation.path} as a looping track")
        ci.cancel()
    }

    fun handleSoundStop(packet: SoundStopCallback.StopInfo, ci: SoundStopCallback.Info) {
        // functionality similar to that of vanilla stopping behaviour (defined in SoundEngine#stop)
        val source = packet.source
        val name = packet.name
        val cancel = if (source != null) {
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

        if (cancel) ci.cancel()
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