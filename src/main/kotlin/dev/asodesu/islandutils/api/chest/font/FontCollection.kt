package dev.asodesu.islandutils.api.chest.font

import net.minecraft.client.gui.font.providers.BitmapProvider
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition
import net.minecraft.resources.Identifier

object FontCollection {
    val collections = mutableMapOf(
        ChestBackgrounds.FONT_KEY.id to ChestBackgrounds,
        Icons.FONT_KEY.id to Icons,
    )

    fun add(font: Identifier, definition: GlyphProviderDefinition) {
        val bitmap = definition as? BitmapProvider.Definition ?: return
        val collection = collections[font] ?: return
        val character = buildString {
            bitmap.codepointGrid[0].forEach { this.appendCodePoint(it) }
        }
        collection.add(bitmap.file, character)
    }

    interface Font {
        fun add(file: Identifier, character: String)
        fun clear()
    }
}