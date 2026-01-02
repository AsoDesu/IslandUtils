package dev.asodesu.islandutils.cosmetics

import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.cosmetics.types.AccessoryCosmetic
import dev.asodesu.islandutils.cosmetics.types.CloakCosmetic
import dev.asodesu.islandutils.cosmetics.types.CosmeticType
import dev.asodesu.islandutils.cosmetics.types.HatCosmetic
import dev.asodesu.islandutils.cosmetics.types.SkinCosmetic
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class Wardrobe {
    val doll = WardrobeDoll(this)
    val slots: List<CosmeticType> = listOf(
        HatCosmetic(),
        AccessoryCosmetic(),
        SkinCosmetic(),
        CloakCosmetic()
    )

    fun apply(guiGraphics: GuiGraphics, livingEntity: LivingEntity) {
        slots.forEach {
            val item = it.get()
            it.render(guiGraphics, livingEntity, item)
        }
    }

    fun isCosmeticItem(item: ItemStack) = slots.any { it.check(item) }
    fun getType(item: ItemStack): CosmeticType? {
        if (item.isEmpty) return null
        return item.customItemId?.let { getType(it) }
    }
    fun getType(itemId: Identifier) = slots.firstOrNull { it.check(itemId) }

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