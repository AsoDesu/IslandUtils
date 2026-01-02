package dev.asodesu.islandutils.features.crafting

import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyserFactory
import dev.asodesu.islandutils.api.chest.anyLineContains
import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.api.chest.lore
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.extentions.getTimeSeconds
import dev.asodesu.islandutils.features.crafting.items.CraftingItem
import dev.asodesu.islandutils.features.crafting.items.SavedCraftingItems
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

class CraftingChestAnalyser(val type: CraftingMenuType) : ChestAnalyser {
    companion object {
        private val INPUT_SLOT_RANGE = 19..23
        private val TIME_LEFT_COLOR = TextColor.parseColor("#FF5556").orThrow
        private const val ACTIVE_CHECK_STRING = "Shift-Click to Cancel"
    }

    override fun analyse(item: ItemStack, slot: Int) {
        if (slot !in INPUT_SLOT_RANGE) return

        val lores = item.lore
        if (lores.isEmpty() || !lores.anyLineContains(ACTIVE_CHECK_STRING)) return removeItem(slot)
        val customItemId = item.customItemId ?: return removeItem(slot)

        // find the "Time Left: " lore line based on it's first child's colour
        val timeLeftComponent = lores.firstOrNull { it.siblings.firstOrNull()?.style?.color == TIME_LEFT_COLOR } ?: return removeItem(slot)
        // add 60 because we don't have the seconds remaining, so we add 60 to assume the worst
        val timeLeft = (timeLeftComponent.string.getTimeSeconds() ?: return removeItem(slot)) + 60
        val finishTimestamp = System.currentTimeMillis() + (timeLeft * 1000L)

        val craftingItem = CraftingItem(
            type = type,
            finishTimestamp = finishTimestamp,
            slot = slot,
            item = CraftingItem.stripItem(item)
        )
        SavedCraftingItems.add(craftingItem)

        debug("[#$slot $type] Found active craft: ${item.displayName.string} + ($timeLeft)")
    }

    fun removeItem(slot: Int) {
        SavedCraftingItems.remove(type, slot)
        debug("$type - Found empty craft slot: $slot!")
    }

    object Factory : ChestAnalyserFactory {
        override fun shouldApply(menuComponents: Collection<Identifier>): Boolean {
            return CraftingMenuType.entries.any { menuComponents.contains(it.menuComponent) }
        }

        override fun create(menuComponents: Collection<Identifier>): ChestAnalyser {
            val type = CraftingMenuType.entries.first { menuComponents.contains(it.menuComponent) }
            return CraftingChestAnalyser(type)
        }
    }

}