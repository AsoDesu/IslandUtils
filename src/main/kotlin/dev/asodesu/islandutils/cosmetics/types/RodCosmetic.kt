package dev.asodesu.islandutils.cosmetics.types

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.game.activeGame
import dev.asodesu.islandutils.cosmetics.item.CosmeticItem
import dev.asodesu.islandutils.games.Fishing
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class RodCosmetic : CosmeticType() {
    override val names = listOf("rods")
    override val badge: Component = Font.ROD_BADGE
    override var base = if (activeGame is Fishing) getFromEntity(minecraft.player!!).toCosmetic() else CosmeticItem.empty()

    override fun getFromEntity(entity: LivingEntity): ItemStack = entity.getItemBySlot(EquipmentSlot.MAINHAND)
    override fun setToEntity(entity: LivingEntity, item: ItemStack) = entity.setItemSlot(EquipmentSlot.MAINHAND, item)

    override fun renderUI(guiGraphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int, cosmeticItem: CosmeticItem): Boolean {
        if (cosmeticItem.isEmpty) return false
        return super.renderUI(guiGraphics, x, y, width, height, cosmeticItem)
    }
}