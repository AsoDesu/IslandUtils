package dev.asodesu.islandutils.api.options.screen

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.options.ConfigSection
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

class SectionScreenTab(private val section: ConfigSection) : ConfigScreenTab {
    override val layout: LinearLayout = LinearLayout.vertical().also { layout ->
        val title = Component.translatable("islandutils.options.${section.name}")
        layout.addChild(StringWidget(title, minecraft.font))
    }

    override fun init() {
    }

    override fun close() {
    }
}