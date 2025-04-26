package dev.asodesu.islandutils.api.sidebar

import net.minecraft.network.chat.Component

fun interface SidebarLineUpdate {
    fun onSidebarLine(component: Component)
}