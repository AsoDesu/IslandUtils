package dev.asodesu.islandutils.cosmetics.types

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class HatCosmetic : CosmeticType() {
    override val names = listOf("hat", "hair")
    override val badge: Component = Font.HAT_BADGE
    override var base = getFromEntity(minecraft.player!!).toCosmetic()

    override fun getFromEntity(entity: LivingEntity): ItemStack = entity.getItemBySlot(EquipmentSlot.HEAD)
    override fun setToEntity(entity: LivingEntity, item: ItemStack) = entity.setItemSlot(EquipmentSlot.HEAD, item)
}