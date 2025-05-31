package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.Slot

interface ContainerScreenHelper {
    fun getMenuComponents(): Collection<ResourceLocation>
    fun getAnalyser(): ChestAnalyser?

    val imageWidth: Int
    val imageHeight: Int
    fun getHoveredSlot(): Slot?
    fun getScreen(): AbstractContainerScreen<*>
}