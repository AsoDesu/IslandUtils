package dev.asodesu.islandutils.api.sidebar

import net.minecraft.network.chat.Component

object SidebarEventsHandler {
    // called from mixin
    fun handleSidebarUpdate(component: Component) {
        SidebarEvents.LINE_UPDATE.invoker().onSidebarLine(component)
    }
}