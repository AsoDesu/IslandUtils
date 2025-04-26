package dev.asodesu.islandutils.api.music.resources

import dev.asodesu.islandutils.api.music.resources.handler.DownloadProgressListener
import dev.asodesu.islandutils.api.options.screen.widgets.background
import dev.asodesu.islandutils.api.ui.ProgressBarWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class DownloadScreen(val download: DownloadProgressListener, val parent: Screen?) : Screen(Component.literal("Downloading")) {
    var progressBar: ProgressBarWidget? = null
    var text: StringWidget? = null

    override fun init() {
        val frame = FrameLayout().apply {
            addRenderableOnly(background(spacing = 7, opacity = .5f))
            addChild(LinearLayout.vertical().spacing(5).apply {
                addChild(FrameLayout().setMinWidth(300).apply {
                    addChild(StringWidget(Component.literal("Downloading Assets"), font)) { it.alignHorizontallyLeft().alignVerticallyMiddle() }
                    addChild(Button.builder(Component.literal("X")){
                        download.cancel()
                    }.width(20).build(), LayoutSettings::alignHorizontallyRight)
                })

                progressBar = addChild(ProgressBarWidget(width = 300, height = 10))
                text = addChild(StringWidget(300, 8, Component.empty(), font).alignLeft())
            })
        }

        frame.arrangeElements()
        FrameLayout.centerInRectangle(frame, this.rectangle)
        frame.visitWidgets { this.addRenderableWidget(it) }
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        progressBar?.progress(download.progress)
        text?.message = download.state

        super.render(guiGraphics, i, j, f)
    }

    override fun shouldCloseOnEsc() = false

    fun close() {
        minecraft!!.setScreen(parent)
    }

}