package dev.asodesu.islandutils.api.options.screen.tab

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.options.ConfigSection
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.network.chat.Component

class SectionScreenTab(private val section: ConfigSection) : ConfigScreenTab {
    override val layout: LinearLayout = LinearLayout.vertical().spacing(3).apply {
        addChild(StringWidget(Component.translatable("islandutils.options.${section.name}"), minecraft.font).alignLeft())
        addChild(SpacerElement.height(0))

        section.children().forEach {
            addChild(it.render(this))
        }
    }

    override fun setMinWidth(width: Int) {
        layout.visitWidgets { it.width = width }
    }

    override fun init() {
    }

    override fun close() {
    }
}