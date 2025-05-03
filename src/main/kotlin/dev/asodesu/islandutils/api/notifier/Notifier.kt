package dev.asodesu.islandutils.api.notifier

import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.style
import net.minecraft.network.chat.Component

object Notifier {
    private val TOOLTIP_TITLE_COMPONENT = Component.translatable("islandutils.notifications.title").style { withUnderlined(true) }
    private val TOOLTIP_GROUP_PADDING = Component.literal("  ")

    private var tooltip: List<Component> = emptyList()
    private val sources = mutableSetOf<NotificationSource>()

    private var ticks = 0
    // called from mixin every tick while multiplayer join screen is open
    fun tickActive() {
        // only run every second
        if ((ticks++ % 20) != 0) return

        val notifications = sources.flatMap { it.provide() }
        if (notifications.isEmpty()) {
            tooltip = emptyList()
            return
        }

        val groupedNotifications = notifications.groupBy { it.source }
        tooltip = buildList {
            add(TOOLTIP_TITLE_COMPONENT)
            add(Component.empty())
            groupedNotifications.forEach { (source, notifs) ->
                add(source.title)
                notifs.forEach { notification ->
                    // add group badding
                    add(buildComponent {
                        append(TOOLTIP_GROUP_PADDING)
                        append(notification.message)
                    })
                }
            }
        }
    }

    // called from mixin when multiplayer join screen opens
    fun activate() {
        ticks = 0
        tickActive()
    }

    fun add(source: NotificationSource) {
        source.title.withStyle { it.withBold(true) }
        sources.add(source)
    }

    fun isEmpty() = tooltip.isEmpty()
    fun get() = tooltip

}