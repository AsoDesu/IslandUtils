package dev.asodesu.islandutils.api.chest

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import kotlin.jvm.optionals.getOrNull

val ItemStack.lore: List<Component>
    get() = this.components.get(DataComponents.LORE)?.lines ?: emptyList()

fun List<Component>.anyLineContains(str: String) = this.any { it.string.contains(str) }

val ItemStack.customData: CustomData?
    get() = this.components.get(DataComponents.CUSTOM_DATA)

val ItemStack.publicBukkitValues: CompoundTag?
    get() = this.customData?.unsafe?.getCompound("PublicBukkitValues")?.getOrNull()

val ItemStack.customItemId: ResourceLocation?
    get() = this.publicBukkitValues?.getString("mcc:custom_item_id")?.getOrNull()?.let { value ->
        ResourceLocation.tryParse(value)
    }