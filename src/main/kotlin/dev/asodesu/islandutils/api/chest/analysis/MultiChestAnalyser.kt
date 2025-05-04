package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class MultiChestAnalyser(private val analysers: List<ChestAnalyser>) : ChestAnalyser {
    override fun analyse(item: ItemStack, slot: Int) = analysers.forEach { it.analyse(item, slot) }
    override fun render(guiGraphics: GuiGraphics, helper: ContainerScreenHelper) = analysers.forEach { it.render(guiGraphics, helper) }
    override fun renderSlot(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) = analysers.forEach { it.renderSlot(guiGraphics, helper, slot) }
}