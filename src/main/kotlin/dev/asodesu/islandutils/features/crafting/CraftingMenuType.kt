package dev.asodesu.islandutils.features.crafting

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.api.Resources
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

enum class CraftingMenuType(path: String, val icon: Component) {
    ASSEMBLER("_fonts/body/blueprint_assembly.png", Font.ASSEMBLER_ICON),
    FORGE("_fonts/body/fusion_forge.png", Font.FUSION_ICON);

    val menuComponent: ResourceLocation = Resources.mcc(path)
}