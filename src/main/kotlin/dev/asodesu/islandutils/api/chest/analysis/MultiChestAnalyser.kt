package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.world.item.ItemStack

class MultiChestAnalyser(private val analysers: List<ChestAnalyser>) : ChestAnalyser {
    override fun analyse(item: ItemStack, slot: Int) = analysers.forEach { it.analyse(item, slot) }
}