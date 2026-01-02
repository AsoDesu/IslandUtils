package dev.asodesu.islandutils.cosmetics.item

import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.api.chest.lore
import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.ItemStack

class CosmeticItem(val item: ItemStack) {
    companion object {
        private val COSMETIC_DESCRIPTION_COLOR = TextColor.parseColor("#768888").orThrow
        fun empty() = CosmeticItem(ItemStack.EMPTY)
    }

    val isEmpty = item.isEmpty
    val customItemId = item.customItemId ?: Resources.islandUtils("empty")
    val tooltip: List<Component>
    val badge: Component?

    init {
        if (isEmpty) {
            tooltip = emptyList()
            badge = null
        } else {
            val lore = item.lore
            val itemName = item.hoverName
            val rarityAndType = lore.firstOrNull() ?: Component.empty()

            val descriptionLines = lore.filter { loreLine ->
                loreLine.siblings.isNotEmpty() && loreLine.siblings.all { it.string.isBlank() || it.style.isItalic && it.style.color == COSMETIC_DESCRIPTION_COLOR }
            }

            tooltip = buildList {
                add(itemName)
                add(rarityAndType)
                add(Component.empty())
                addAll(descriptionLines)
            }
            badge = rarityAndType.toFlatList().getOrNull(1)
        }
    }
}