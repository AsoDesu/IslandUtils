package dev.asodesu.islandutils.api.options.download

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.music.resources.handler.DownloadJob
import dev.asodesu.islandutils.api.options.option.Option
import dev.asodesu.islandutils.api.options.option.OptionRenderer
import dev.asodesu.islandutils.api.rect
import dev.asodesu.islandutils.api.scissor
import dev.asodesu.islandutils.api.ui.ProgressBarWidget
import dev.asodesu.islandutils.api.ui.tween.Easing
import dev.asodesu.islandutils.api.ui.tween.Tween
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import kotlin.time.Duration.Companion.seconds

class DownloaderOptionRenderer<T>(private val renderer: OptionRenderer<T>, private val job: DownloadJob) : OptionRenderer<T> {

    override fun render(option: Option<T>, layout: Layout): LayoutElement {
        return LinearLayout.vertical().apply {
            addChild(renderer.render(option, layout))
            addChild(DownloadSectionRenderer(job, layout))
        }
    }

    class DownloadSectionRenderer(val job: DownloadJob, val parentLayout: Layout) : AbstractWidget(0, 0, 150, 0, Component.empty()) {
        private val PADDING = 8
        private val TWEEN_DURATION = 0.2.seconds

        private var didHaveJob = false
        private var heightTween: Tween<Int>? = null

        private val widgets = mutableListOf<AbstractWidget>()
        private val layout: Layout
        private val stringWidget: StringWidget
        private val progressBar: ProgressBarWidget

        init {
            layout = LinearLayout.vertical().spacing(3).apply {
                stringWidget = addChild(StringWidget(150, 8, Component.empty(), minecraft.font).alignLeft())
                progressBar = addChild(ProgressBarWidget(height = 8, backgroundColor = ARGB.color(255, 25, 30, 51)))
            }
            arrangeLayout()
            height = targetHeight()
            active = false
            layout.visitWidgets { widgets += it }
        }

        override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
            tickTween(f)

            val currentDownload = job.currentDownload
            if (currentDownload == null) {
                if (didHaveJob) {
                    heightTween = Tween.int(height, targetHeight(), TWEEN_DURATION, easing = Easing.EASE_OUT_EXPO)
                    didHaveJob = false
                }
                return
            } else if (!didHaveJob) {
                heightTween = Tween.int(height, targetHeight(), TWEEN_DURATION, easing = Easing.EASE_OUT_EXPO)
                didHaveJob = true
            }

            guiGraphics.scissor(x, y, width, height) {
                guiGraphics.rect(x, y, width, height, ARGB.color(45, 54, 91))

                stringWidget.message = currentDownload.state
                progressBar.progress(currentDownload.progress)

                widgets.forEach { it.render(guiGraphics, i, j, f) }
            }
        }

        private fun tickTween(tickDeta: Float) {
            val tween = heightTween ?: return
            height = tween.tick(tickDeta)
            parentLayout.arrangeElements()
            if (tween.finished) heightTween = null
        }

        private fun targetHeight() = if (job.currentDownload != null) (layout.height + PADDING * 2) else 0

        private fun arrangeLayout() {
            layout.x = x + PADDING
            layout.y = y + PADDING
            layout.arrangeElements()
        }

        override fun setX(i: Int) {
            super.setX(i)
            arrangeLayout()
        }
        override fun setY(i: Int) {
            super.setY(i)
            arrangeLayout()
        }
        override fun setWidth(i: Int) {
            super.setWidth(i)
            layout.visitWidgets { it.width = (i - PADDING * 2) }
        }

        override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        }

    }

}