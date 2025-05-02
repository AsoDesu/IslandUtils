package dev.asodesu.islandutils.api.chest.font

import net.minecraft.client.gui.font.providers.BitmapProvider
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition
import net.minecraft.resources.ResourceLocation

object FontCollection {
    val collections = mutableMapOf(
        ChestBackgrounds.FONT_KEY to ChestBackgrounds
    )

    fun add(font: ResourceLocation, definition: GlyphProviderDefinition) {
        val bitmap = definition as? BitmapProvider.Definition ?: return
        val collection = collections[font] ?: return
        val character = buildString {
            bitmap.codepointGrid[0].forEach { this.appendCodePoint(it) }
        }
        collection.add(bitmap.file, character)
    }

    interface Font {
        fun add(file: ResourceLocation, character: String)
        fun clear()
    }
}