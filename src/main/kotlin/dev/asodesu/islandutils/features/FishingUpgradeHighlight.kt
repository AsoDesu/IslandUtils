package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyserFactory
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper
import dev.asodesu.islandutils.api.chest.anyLineContains
import dev.asodesu.islandutils.api.chest.loreOrNull
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.pose
import dev.asodesu.islandutils.api.game.activeGame
import dev.asodesu.islandutils.games.Fishing
import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.Slot

class FishingUpgradeHighlight : ChestAnalyser {
    private val UPGRADE_SPRITE = Resources.islandUtils("upgrade")
    private val UPGRADE_LORE_LINE = "Left-Click to Upgrade"

    override fun renderSlot(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) {
        if (!slot.hasItem()) return
        val lores = slot.item.loreOrNull ?: return
        if (!lores.anyLineContains(UPGRADE_LORE_LINE)) return

        guiGraphics.pose {
            translate(0f, 0f, 200f)
            guiGraphics.blitSprite(RenderType::guiTextured, UPGRADE_SPRITE, slot.x + 1, slot.y + 1, 16, 16)
        }
    }

    companion object : ChestAnalyserFactory {
        private val enabled by MiscOptions.scavengingTotals
        override fun shouldApply(menuComponents: Collection<ResourceLocation>) = enabled && activeGame is Fishing
        override fun create(menuComponents: Collection<ResourceLocation>) = FishingUpgradeHighlight()
    }
}