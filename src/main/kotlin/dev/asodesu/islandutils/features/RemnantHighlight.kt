package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper
import dev.asodesu.islandutils.api.chest.anyLineContains
import dev.asodesu.islandutils.api.chest.loreOrNull
import dev.asodesu.islandutils.api.extentions.rect
import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.ARGB
import net.minecraft.world.inventory.Slot

object RemnantHighlight : ChestAnalyser {
    private const val REMNANT_STRING = "This item is the remnant of an item"
    private val REMNANT_HIGHLIGHT_COLOR = ARGB.color(192, 141, 65, 100)
    private val enabled by MiscOptions.remnantHighlight

    override fun renderSlotBack(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) {
        if (!enabled || !slot.hasItem()) return
        val lores = slot.item.loreOrNull ?: return
        if (!lores.anyLineContains(REMNANT_STRING)) return
        guiGraphics.rect(slot.x, slot.y, 16, 16, REMNANT_HIGHLIGHT_COLOR)
    }
}