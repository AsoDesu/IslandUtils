package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

interface ChestAnalyser {
    fun analyse(item: ItemStack, slot: Int) {}
    fun render(guiGraphics: GuiGraphics, helper: ContainerScreenHelper) {}
    fun renderSlot(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) {}
}