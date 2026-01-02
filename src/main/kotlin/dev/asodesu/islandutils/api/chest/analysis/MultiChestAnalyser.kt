package dev.asodesu.islandutils.api.chest.analysis

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class MultiChestAnalyser(private val analysers: List<ChestAnalyser>) : ChestAnalyser {
    override fun analyse(item: ItemStack, slot: Int) = analysers.forEach { it.analyse(item, slot) }
    override fun tick(helper: ContainerScreenHelper) = analysers.forEach { it.tick(helper) }
    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, helper: ContainerScreenHelper) = analysers.forEach { it.render(guiGraphics, mouseX, mouseY, helper) }
    override fun renderSlotFront(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) = analysers.forEach { it.renderSlotFront(guiGraphics, helper, slot) }
    override fun renderSlotBack(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) = analysers.forEach { it.renderSlotBack(guiGraphics, helper, slot) }
    override fun mouseDragged(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) = analysers.forEach { it.mouseDragged(helper, mouseX, mouseY, deltaX, deltaY) }
    override fun keyPressed(helper: ContainerScreenHelper, keyCode: Int, scanCode: Int, modifiers: Int) = analysers.forEach { it.keyPressed(helper, keyCode, scanCode, modifiers) }
    override fun mouseReleased(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, keyCode: Int) = analysers.forEach { it.mouseReleased(helper, mouseX, mouseY, keyCode) }
    override fun close(helper: ContainerScreenHelper) = analysers.forEach { it.close(helper) }
}