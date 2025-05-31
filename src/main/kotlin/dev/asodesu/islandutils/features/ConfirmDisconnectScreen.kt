package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.ui.background
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component.translatable

class ConfirmDisconnectScreen(
    private val parent: Screen,
    private val disconnect: Button.OnPress
) : Screen(translatable("islandutils.confirm_disconnect.title")) {

    override fun init() {
        val frame = FrameLayout().apply {
            addRenderableOnly(background(spacing = 12, opacity = .5f))
            addChild(LinearLayout.vertical().spacing(10).apply {
                addChild(StringWidget(title, font)) { it.alignHorizontallyCenter() }
                addChild(LinearLayout.horizontal().spacing(10).apply {
                    addChild(Button.builder(translatable("gui.cancel")) { onClose() }.width(120).build())
                    addChild(Button.builder(translatable("islandutils.confirm_disconnect.disconnect"), disconnect).width(120).build())
                })
            })
        }

        frame.arrangeElements()
        FrameLayout.centerInRectangle(frame, this.rectangle)
        frame.visitWidgets { this.addRenderableWidget(it) }
    }

    override fun onClose() {
        minecraft?.setScreen(parent)
    }

}