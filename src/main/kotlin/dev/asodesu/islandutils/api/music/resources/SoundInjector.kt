package dev.asodesu.islandutils.api.music.resources

import dev.asodesu.islandutils.api.extentions.minecraft
import java.nio.file.Path
import net.minecraft.client.resources.sounds.Sound
import net.minecraft.client.sounds.WeighedSoundEvents
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.server.packs.resources.Resource
import net.minecraft.util.valueproviders.ConstantFloat

object SoundInjector {
    private val registryOverrides = mutableMapOf<Identifier, WeighedSoundEvents>()
    private val soundCacheOverrides = mutableMapOf<Identifier, Resource>()
    private val islandUtilsPackResources = IslandUtilsPackResources()

    fun inject(location: Identifier, path: Path) {
        val fileLocation = Identifier.fromNamespaceAndPath(location.namespace, location.path + "_file")
        val soundFileLocation = Sound.SOUND_LISTER.idToFile(fileLocation)

        val resource = Resource(islandUtilsPackResources, IoSupplier.create(path))

        val soundEvents = WeighedSoundEvents(location, "")
        soundEvents.addSound(
            Sound(
                fileLocation, // sound file location
                ConstantFloat.of(1f), // volume
                ConstantFloat.of(1f), // pitch
                1, // weight
                Sound.Type.FILE, // sound type (unused)
                true, // stream
                false, // preload
                16 // attenuation distance
            )
        )
        registryOverrides[location] = soundEvents
        soundCacheOverrides[soundFileLocation] = resource

        val soundManager = minecraft.soundManager ?: return
        soundManager.registry[location] = soundEvents
        soundManager.soundCache[soundFileLocation] = resource
    }

    fun apply() {
        val soundManager = minecraft.soundManager
        soundManager.registry.putAll(registryOverrides)
        soundManager.soundCache.putAll(soundCacheOverrides)
    }

}