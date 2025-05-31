package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.options.screen.tab.SectionScreenTab
import net.minecraft.client.gui.layouts.Layout

abstract class ConfigSection(name: String) : ConfigGroup(name) {
    override fun render(layout: Layout) = SectionScreenTab(this)
}