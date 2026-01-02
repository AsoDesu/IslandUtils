package dev.asodesu.islandutils.cosmetics

import com.mojang.blaze3d.platform.InputConstants
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyserFactory
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper
import dev.asodesu.islandutils.api.chest.anyLineContains
import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.api.chest.lore
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.game.inGame
import dev.asodesu.islandutils.cosmetics.item.CosmeticItem
import dev.asodesu.islandutils.cosmetics.types.CosmeticType
import dev.asodesu.islandutils.options.CosmeticOptions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.ceil

class CosmeticUI(private val wardrobe: Wardrobe) : ChestAnalyser {
    private val PREVIEW_SPRITE = Resources.islandUtils("preview")
    private val SELECTED_COSMETIC_LINE = "Click to Unequip"
    private val UI_DOLL_BOTTOM_PADDING = 8
    private val UI_DISPLAY_GAP = 4

    private var isCosmeticMenu = false
    private var lastHoveredSlot = -1
    private var lastHoveredItemType = Identifier.withDefaultNamespace("default")
    private var previewKeyMapping: Int = InputConstants.MOUSE_BUTTON_MIDDLE

    override fun analyse(item: ItemStack, slot: Int) {
        val type = wardrobe.getType(item) ?: return

        // check for new base items
        if (item.lore.anyLineContains(SELECTED_COSMETIC_LINE)) {
            type.baseItem = item
        }

        isCosmeticMenu = true
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, helper: ContainerScreenHelper) {
        if (shouldRender()) return

        // update hovered item
        if (CosmeticOptions.showOnHover.get()) {
            val hoveredSlot = helper.getHoveredSlot()
            val item = hoveredSlot?.item ?: ItemStack.EMPTY
            // only trigger updates when we change what slot we're hovering
            val hoveredSlotIndex = hoveredSlot?.index ?: -1
            val hoveredItemType = item.customItemId ?: Identifier.withDefaultNamespace("default")
            if (lastHoveredSlot != hoveredSlotIndex || lastHoveredItemType != hoveredItemType) {
                wardrobe.slots.forEach { it.checkAndUpdateHover(item) }
                lastHoveredSlot = hoveredSlotIndex
                lastHoveredItemType = hoveredItemType
            }
        }

        val screen = helper.getScreen()

        val size = ceil(helper.imageHeight / 2.5).toInt()
        val bounds = helper.imageHeight
        var x = (screen.width - helper.imageWidth) / 3
        var y = (screen.height / 2)

        wardrobe.doll.render(
            guiGraphics,
            x - bounds,
            y - bounds,
            x + bounds,
            y + bounds,
            size
        )

        x -= (size / 2) // offset by size/2 to be placed in the middle of the doll
        y += size // offset by size to be placed below the doll
        y += UI_DOLL_BOTTOM_PADDING
        wardrobe.slots.forEach {
            if (!it.renderUI(guiGraphics, x, y, size)) return@forEach
            y += CosmeticType.UI_DISPLAY_HEIGHT + UI_DISPLAY_GAP
        }
    }

    override fun mouseDragged(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) {
        if (shouldRender()) return

        wardrobe.doll.xRot -= deltaX.toFloat()
        wardrobe.doll.yRot -= deltaY.toFloat()
    }

    override fun renderSlotFront(guiGraphics: GuiGraphics, helper: ContainerScreenHelper, slot: Slot) {
        if (shouldRender()) return
        val item = slot.item
        val customItemId = item.customItemId ?: return
        val type = wardrobe.getType(customItemId) ?: return

        // check if this item is this types preview item
        if (type.preview.customItemId != customItemId) return

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PREVIEW_SPRITE, slot.x - 3, slot.y - 4, 22, 24)
    }

    fun shouldRender(): Boolean {
        if (!CosmeticOptions.showInGames.get() && inGame) return false
        return !CosmeticOptions.showInAllMenus.get() && !isCosmeticMenu
    }

    override fun keyPressed(helper: ContainerScreenHelper, keyCode: Int, scanCode: Int, modifiers: Int) = testPreviewClicked(helper, keyCode)
    override fun mouseReleased(helper: ContainerScreenHelper, mouseX: Double, mouseY: Double, keyCode: Int) = testPreviewClicked(helper, keyCode)
    private fun testPreviewClicked(helper: ContainerScreenHelper, keyCode: Int) {
        if (!isCosmeticMenu) return
        if (keyCode != previewKeyMapping) return

        val hoveredSlot = helper.getHoveredSlot() ?: return
        val item = hoveredSlot.item ?: return
        val cosmeticType = wardrobe.getType(item) ?: return

        // if we are clicking on the same item again, clear the preview for this type
        if (cosmeticType.preview.customItemId == item.customItemId) {
            cosmeticType.preview = CosmeticItem.empty()
            cosmeticType.hover = CosmeticItem.empty()
        } else {
            cosmeticType.previewItem = item
        }
    }

    override fun close(helper: ContainerScreenHelper) {
        Wardrobe.dispose()
    }

    object Factory : ChestAnalyserFactory {
        override fun create(menuComponents: Collection<Identifier>) = CosmeticUI(Wardrobe.get())
        override fun shouldApply(menuComponents: Collection<Identifier>) = CosmeticOptions.enabled.get()
    }
}