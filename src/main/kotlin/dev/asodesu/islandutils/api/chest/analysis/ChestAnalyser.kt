package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

interface ChestAnalyser {
    fun analyse(item: ItemStack, slot: Int) {}
    fun tick(helper: ContainerScreenHelper) {}
    fun render(guiGraphics: GuiGraphics, helper: ContainerScreenHelper) {}
    fun renderSlot(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) {}
    fun mouseDragged(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) {}
    fun mouseReleased(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, keyCode: Int) {}
    fun keyPressed(helper: ContainerScreenHelper, keyCode: Int, scanCode: Int, modifiers: Int) {}
    fun close(helper: ContainerScreenHelper) {}
}