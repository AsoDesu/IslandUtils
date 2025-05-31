package dev.asodesu.islandutils.api.json

import com.mojang.serialization.Codec
import net.minecraft.world.item.ItemStack

object ItemStackSerializer : JsonCodecSerializer<ItemStack>() {
    override val codec: Codec<ItemStack> = ItemStack.SINGLE_ITEM_CODEC
}