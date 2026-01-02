package dev.asodesu.islandutils.api.chest

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import kotlin.jvm.optionals.getOrNull

val ItemStack.loreOrNull: List<Component>?
    get() = this.components.get(DataComponents.LORE)?.lines
val ItemStack.lore: List<Component>
    get() = loreOrNull ?: emptyList()

fun List<Component>.anyLineContains(str: String) = this.any { it.string.contains(str) }

val ItemStack.customData: CustomData?
    get() = this.components.get(DataComponents.CUSTOM_DATA)

val ItemStack.publicBukkitValues: CompoundTag?
    get() = this.customData?.tag?.getCompound("PublicBukkitValues")?.getOrNull()

val ItemStack.customItemId: Identifier?
    get() = this.get(DataComponents.ITEM_MODEL)