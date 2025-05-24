package dev.asodesu.islandutils.cosmetics.types

import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.isInsideBox
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.extentions.pose
import dev.asodesu.islandutils.cosmetics.item.CosmeticItem
import java.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ARGB
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

abstract class CosmeticType {
    companion object {
        val BACKGROUND_SPRITE = Resources.islandUtils("widget/rounded_1")
        val BACKGROUND_COLOR = ARGB.color(76, 0, 0, 0)
        const val UI_BADGE_PADDING_X = 4
        const val UI_ITEM_PADDING = 2
        const val UI_DISPLAY_HEIGHT = 20
        const val ITEM_SIZE = 16
    }

    abstract val names: List<String>
    abstract val badge: Component?

    open var base: CosmeticItem = CosmeticItem.empty()
    var baseItem: ItemStack
        get() = base.item
        set(value) { base = value.toCosmetic() }

    open var hover: CosmeticItem = CosmeticItem.empty()
    var hoverItem: ItemStack
        get() = hover.item
        set(value) { hover = value.toCosmetic() }

    open var preview: CosmeticItem = CosmeticItem.empty()
    var previewItem: ItemStack
        get() = preview.item
        set(value) { preview = value.toCosmetic() }

    abstract fun getFromEntity(entity: LivingEntity): ItemStack
    abstract fun setToEntity(entity: LivingEntity, item: ItemStack)
    open fun render(entity: LivingEntity, cosmeticItem: CosmeticItem) = setToEntity(entity, cosmeticItem.item)

    open fun renderUI(guiGraphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int = UI_DISPLAY_HEIGHT, cosmeticItem: CosmeticItem = get()) {
        val badge = badge ?: return
        val font = minecraft.font

        val itemX = width - ITEM_SIZE - UI_ITEM_PADDING
        val itemY = UI_ITEM_PADDING

        guiGraphics.pose {
            translate(x.toDouble(), y.toDouble(), 0.0)
            guiGraphics.blitSprite(RenderType::guiTextured, BACKGROUND_SPRITE, 0, 0, width, height, BACKGROUND_COLOR)
            guiGraphics.drawString(font, badge, UI_BADGE_PADDING_X, ((height - font.lineHeight) / 2)+1, ARGB.white(1f))
            guiGraphics.renderItem(cosmeticItem.item, itemX, itemY)
        }

        val mouseHandler = minecraft.mouseHandler
        val window = minecraft.window
        val xPos = mouseHandler.getScaledXPos(window).toInt()
        val yPos = mouseHandler.getScaledYPos(window).toInt()
        if (isInsideBox(xPos, yPos, x + itemX, y + itemY, ITEM_SIZE, ITEM_SIZE)) {
            guiGraphics.renderTooltip(minecraft.font, cosmeticItem.tooltip, Optional.empty(), xPos, yPos)
        }
    }

    fun checkAndUpdateHover(item: ItemStack) {
        hover = if (check(item)) item.toCosmetic()
        else CosmeticItem.empty()
    }

    fun get(): CosmeticItem {
        if (!hover.isEmpty) return hover
        if (!preview.isEmpty) return preview
        if (!base.isEmpty) return base
        return CosmeticItem.empty()
    }

    fun check(item: ItemStack): Boolean {
        val itemId = item.customItemId ?: return false
        return check(itemId)
    }
    fun check(itemId: ResourceLocation) = names.any { itemId.path?.contains(it) == true }

    fun ItemStack.toCosmetic() = CosmeticItem(this)
}