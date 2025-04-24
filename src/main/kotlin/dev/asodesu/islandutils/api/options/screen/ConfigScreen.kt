package dev.asodesu.islandutils.api.options.screen

import dev.asodesu.islandutils.api.options.Config
import dev.asodesu.islandutils.api.options.ConfigEntry
import dev.asodesu.islandutils.api.options.ConfigSection
import dev.asodesu.islandutils.api.options.screen.tab.ConfigScreenTab
import dev.asodesu.islandutils.api.options.screen.widgets.BackgroundWidget
import dev.asodesu.islandutils.api.options.screen.widgets.FlatButton
import dev.asodesu.islandutils.api.options.screen.widgets.background
import java.util.concurrent.CompletableFuture
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.math.min

class ConfigScreen(
    private val parent: Screen,
    private val config: Config,
    private val entries: List<ConfigEntry>
) : Screen(Component.literal("IslandUtils Options")) {
    // consts
    val BACKGROUND_OPACITY = .5f
    val TAB_BACKGROUND_OPACITY = .75f
    val TAB_BUTTONS_PADDING = 3
    val CONTAINER_PADDING = 7
    val HEIGHT = 300

    lateinit var screenLayout: LinearLayout
    lateinit var tabsFrame: FrameLayout
    lateinit var configFrame: FrameLayout
    lateinit var statusWidget: StringWidget

    val tabButtons = mutableMapOf<ConfigSection, Button>()
    var tab: ConfigScreenTab? = null

    override fun init() {
        var firstTab: ConfigSection? = null
        tabsFrame = FrameLayout().apply {
            addRenderableOnly(background(spacing = CONTAINER_PADDING, opacity = TAB_BACKGROUND_OPACITY, sprite = BackgroundWidget.LEFT))

            // tabs button
            addChild(LinearLayout.vertical().spacing(TAB_BUTTONS_PADDING).apply {
                addChild(SpacerElement.height(-2)) // jank :3
                addChild(StringWidget(this@ConfigScreen.title, minecraft!!.font))
                addChild(SpacerElement.height(0)) // height of 0 since this adds to the padding

                entries.forEach { entry ->
                    if (entry !is ConfigSection) return@forEach
                    if (firstTab == null) firstTab = entry

                    val translatable = Component.translatable("islandutils.options.${entry.name}")
                    val tabButton = Button.builder(translatable) { _ -> switchTab(entry) }.build()
                    addChild(tabButton)

                    tabButtons[entry] = tabButton
                }
            }, LayoutSettings::alignVerticallyTop)

            addChild(LinearLayout.vertical().apply {
                statusWidget = addChild(StringWidget(Component.empty(), minecraft!!.font).alignLeft())
                statusWidget.width = Button.DEFAULT_WIDTH

                val doneComponent = Component.literal("Done")
                addChild(Button.builder(doneComponent) { saveAndClose() }.build(), LayoutSettings::alignVerticallyBottom)
            }, LayoutSettings::alignVerticallyBottom)
        }

        configFrame = FrameLayout().apply {
            addRenderableOnly(background(spacing = CONTAINER_PADDING, opacity = BACKGROUND_OPACITY, sprite = BackgroundWidget.RIGHT))
        }

        screenLayout = LinearLayout.horizontal().spacing(CONTAINER_PADDING * 2).apply {
            addChild(tabsFrame)
            addChild(configFrame)
            visitWidgets { this@ConfigScreen.addRenderableWidget(it) }
        }

        firstTab?.let { tab -> switchTab(tab) }
        this.repositionElements()
    }

    override fun repositionElements() {
        tabsFrame.arrangeElements()
        val minHeight = minHeight()
        val minWidth = minWidth()

        tabsFrame.setMinHeight(minHeight)
        configFrame.setMinHeight(minHeight)
        configFrame.setMinWidth(minWidth)

        screenLayout.arrangeElements()
        FrameLayout.centerInRectangle(screenLayout, this.rectangle)

        tab?.let { tab ->
            val tabLayout = tab.layout
            tabLayout.x = configFrame.x
            tabLayout.y = configFrame.y
            tab.setMinWidth(minWidth)
            tabLayout.arrangeElements()
        }
    }

    fun minHeight() = min(HEIGHT, this.height - 25)
    fun minWidth() = if (this.width >= (400 + tabsFrame.width)) 400 else this.width - tabsFrame.width - 50

    private fun switchTab(section: ConfigSection) {
        if (tab != null) {
            tab!!.layout.visitWidgets { this.removeWidget(it) }
            tab!!.close()
        }

        tab = section.render().apply {
            init()
            x = configFrame.x
            y = configFrame.y
            setMinWidth(minWidth())
            this.layout.arrangeElements()
            visitWidgets { addRenderableWidget(it) }
        }

        tabButtons.forEach { (buttonSection, button) ->
            button.isFocused = buttonSection == section
        }
    }

    fun saveAndClose() {
        statusWidget.message = Component.literal("Saving...").withStyle(ChatFormatting.GREEN)
        CompletableFuture.runAsync {
            config.save()
            minecraft!!.submit { close() }
        }
    }

    fun close() {
        tab?.close()
        minecraft?.setScreen(parent)
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        // taken from super method

        // this fixes an annoying bug? in minecraft where the buttons
        //  remain focussed after you click them, which makes our flat
        //  buttons look REAL bad

        val optional = this.getChildAt(d, e)
        if (optional.isEmpty) {
            return false
        } else {
            val guiEventListener = optional.get()
            if (guiEventListener.mouseClicked(d, e, i)) {
                if (guiEventListener !is FlatButton) this.focused = guiEventListener
                else this.focused = null
                if (i == 0) this.isDragging = true
            }
            return true
        }
    }

}