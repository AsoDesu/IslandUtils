package dev.asodesu.islandutils.cosmetics

import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.cosmetics.types.AccessoryCosmetic
import dev.asodesu.islandutils.cosmetics.types.CosmeticType
import dev.asodesu.islandutils.cosmetics.types.HatCosmetic
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class Wardrobe {
    val doll = WardrobeDoll(this)
    val slots: List<CosmeticType> = listOf(
        HatCosmetic(),
        AccessoryCosmetic()
    )

    fun apply(livingEntity: LivingEntity) {
        slots.forEach {
            val item = it.get()
            it.render(livingEntity, item)
        }
    }

    fun isCosmeticItem(item: ItemStack) = slots.any { it.check(item) }
    fun getType(item: ItemStack): CosmeticType? {
        if (item.isEmpty) return null
        return item.customItemId?.let { getType(it) }
    }
    fun getType(itemId: ResourceLocation) = slots.firstOrNull { it.check(itemId) }

    companion object {
        private var instance: Wardrobe? = null
        fun get(): Wardrobe {
            if (instance != null) return instance!!
            return Wardrobe().also {
                instance = it
            }
        }

        fun dispose() {
            instance = null
        }
    }
}