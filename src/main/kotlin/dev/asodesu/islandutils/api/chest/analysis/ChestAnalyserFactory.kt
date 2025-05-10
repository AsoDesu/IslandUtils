package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.resources.ResourceLocation

interface ChestAnalyserFactory {
    fun shouldApply(menuComponents: Collection<ResourceLocation>): Boolean
    fun create(menuComponents: Collection<ResourceLocation>): ChestAnalyser
}