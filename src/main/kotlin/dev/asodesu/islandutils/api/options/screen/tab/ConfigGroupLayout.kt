package dev.asodesu.islandutils.api.options.screen.tab

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.ui.LayoutDelegate
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement

class ConfigGroupLayout(private val group: ConfigGroup) : LayoutDelegate() {
    override val layout: LinearLayout = LinearLayout.vertical().spacing(3).apply {
        addChild(SpacerElement.height(2))
        addChild(StringWidget(group.component, minecraft.font).alignLeft())
        addChild(LinearLayout.vertical().spacing(3).apply {
            group.children().forEach {
                addChild(it.render())
            }
        })
    }
}