package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.resources.Identifier

interface ChestAnalyserFactory {
    fun shouldApply(menuComponents: Collection<Identifier>): Boolean
    fun create(menuComponents: Collection<Identifier>): ChestAnalyser
}