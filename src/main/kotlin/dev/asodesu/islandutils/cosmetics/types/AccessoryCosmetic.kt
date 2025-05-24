package dev.asodesu.islandutils.cosmetics.types

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class AccessoryCosmetic : CosmeticType() {
    override val names = listOf("accessory")
    override val badge: Component = Font.ACCESSORY_BADGE
    override var base = getFromEntity(minecraft.player!!).toCosmetic()

    override fun getFromEntity(entity: LivingEntity): ItemStack = entity.getItemBySlot(EquipmentSlot.OFFHAND)
    override fun setToEntity(entity: LivingEntity, item: ItemStack) = entity.setItemSlot(EquipmentSlot.OFFHAND, item)
}