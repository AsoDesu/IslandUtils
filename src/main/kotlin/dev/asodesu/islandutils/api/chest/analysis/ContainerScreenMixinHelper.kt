package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.resources.ResourceLocation

interface ContainerScreenMixinHelper {
    fun getMenuComponents(): Collection<ResourceLocation>
    fun getAnalyser(): ChestAnalyser?
}