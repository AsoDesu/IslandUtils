package dev.asodesu.islandutils.cosmetics.types

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.cosmetics.cloak.CloakRenderLayer
import dev.asodesu.islandutils.cosmetics.item.CosmeticItem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class CloakCosmetic : CosmeticType() {
    override val names = listOf("back")
    override val badge: Component = Font.CLOAK_BADGE
    override var base = CosmeticItem.empty()

    override fun render(guiGraphics: GuiGraphics, entity: LivingEntity, cosmeticItem: CosmeticItem) {
        CloakRenderLayer.cloakForNextRender = if (cosmeticItem.isEmpty) null else cosmeticItem.item
        CloakRenderLayer.playerEntityForNextRender = entity
    }

    override fun getFromEntity(entity: LivingEntity): ItemStack = ItemStack.EMPTY
    override fun setToEntity(entity: LivingEntity, item: ItemStack) {}
    override fun renderUI(guiGraphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int, cosmeticItem: CosmeticItem): Boolean {
        if (cosmeticItem.isEmpty) return false
        return super.renderUI(guiGraphics, x, y, width, height, cosmeticItem)
    }
}