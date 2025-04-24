package dev.asodesu.islandutils.api.options.screen

import java.util.function.Consumer
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutSettings

fun Layout.frameLayout(settings: Consumer<LayoutSettings>? = null, config: FrameLayout.() -> Unit): FrameLayout {
    val frame = FrameLayout()
    config(frame)

    this.arrangeElements()
    if (settings != null) frame.addChild(this, settings)
    else frame.addChild(this)
    frame.arrangeElements()
    return frame
}