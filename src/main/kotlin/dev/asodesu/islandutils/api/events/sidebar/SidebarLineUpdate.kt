package dev.asodesu.islandutils.api.events.sidebar

import net.minecraft.network.chat.Component

fun interface SidebarLineUpdate {
    fun onSidebarLine(component: Component)
}