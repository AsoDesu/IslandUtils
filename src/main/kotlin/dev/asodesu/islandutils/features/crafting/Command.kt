package dev.asodesu.islandutils.features.crafting

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import dev.asodesu.islandutils.api.debugMode
import dev.asodesu.islandutils.api.style
import dev.asodesu.islandutils.features.crafting.items.CraftingItem
import dev.asodesu.islandutils.features.crafting.items.SavedCraftingItems
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.CustomModelData

fun craftingDebugCommand() = literal("crafting")
    .then(craftingDebugAddCraftCommand())

enum class RarityColors(color: String) {
    Common("#FFFFFF"),
    Uncommon("#1EFF00"),
    Rare("#0070DD"),
    Epic("#A335EE"),
    Legendary("#FF8000"),
    Mythic("#F94242");

    val textColor = TextColor.parseColor(color).orThrow
}
private fun craftingDebugAddCraftCommand() = literal("add_craft")
    .then(argument("rarity", IntegerArgumentType.integer(0, 5))
    .then(argument("menu", IntegerArgumentType.integer(0, 1))
    .then(argument("name", StringArgumentType.string())
    .then(argument("delay", IntegerArgumentType.integer())
    .then(argument("slot", IntegerArgumentType.integer())
    .executes { ctx ->
        if (!debugMode) return@executes 0

        val rarity = RarityColors.entries[ctx.getArgument("rarity", Int::class.javaObjectType)]
        val menuType = CraftingMenuType.entries[ctx.getArgument("menu", Int::class.javaObjectType)]
        val name = ctx.getArgument("name", String::class.java)
        val delay = ctx.getArgument("delay", Int::class.javaObjectType)
        val slot = ctx.getArgument("slot", Int::class.javaObjectType)

        val item = ItemStack(Items.ECHO_SHARD).apply {
            set(DataComponents.CUSTOM_MODEL_DATA, CustomModelData(listOf(9415f), emptyList(), emptyList(), emptyList()))
            set(DataComponents.ITEM_NAME, Component.literal(name).style { withColor(rarity.textColor) })
        }

        val craftingItem = CraftingItem(
            type = menuType,
            finishTimestamp = System.currentTimeMillis() + (delay * 1000L),
            slot = slot,
            item = item
        )
        SavedCraftingItems.add(craftingItem)

        1
    })))))