package dev.asodesu.islandutils.features.crafting.items

import dev.asodesu.islandutils.features.crafting.CraftingMenuType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack

@Serializable
data class CraftingItem(
    val type: CraftingMenuType,
    val finishTimestamp: Long,
    val slot: Int,
    @Contextual val item: ItemStack,
    var notified: Boolean = false,
) {
    companion object {
        private val STRIPPED_COMPONENTS = listOf(DataComponents.LORE, DataComponents.TOOLTIP_DISPLAY)
        fun stripItem(item: ItemStack): ItemStack {
            return item.copy().also { strippedItem ->
                STRIPPED_COMPONENTS.forEach { strippedItem.remove(it) }
            }
        }
    }

    fun isComplete() = System.currentTimeMillis() > finishTimestamp
}