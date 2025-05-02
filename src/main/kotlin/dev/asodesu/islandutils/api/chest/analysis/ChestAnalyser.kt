package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.world.item.ItemStack

interface ChestAnalyser {
    fun analyse(item: ItemStack, slot: Int)
}