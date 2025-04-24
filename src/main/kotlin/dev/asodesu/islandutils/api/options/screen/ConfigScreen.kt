package dev.asodesu.islandutils.api.options.screen

import dev.asodesu.islandutils.api.options.Config
import dev.asodesu.islandutils.api.options.ConfigEntry
import dev.asodesu.islandutils.api.options.ConfigSection
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
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

            val doneComponent = Component.literal("Done")
            addChild(Button.builder(doneComponent) { close() }.build(), LayoutSettings::alignVerticallyBottom)
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

        val sectionScreenTab = section.render()
        sectionScreenTab.init()

        val layout = sectionScreenTab.layout
        layout.x = configFrame.x
        layout.y = configFrame.y

        layout.arrangeElements()
        layout.visitWidgets { this.addRenderableWidget(it) }

        tabButtons.forEach { (buttonSection, button) ->
            button.isFocused = buttonSection == section
        }
        tab = sectionScreenTab
    }

    fun close() {
        tab?.close()
        minecraft?.setScreen(parent)
    }

}