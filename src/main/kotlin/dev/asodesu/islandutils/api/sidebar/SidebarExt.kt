package dev.asodesu.islandutils.api.sidebar

import dev.asodesu.islandutils.api.events.EventKey

fun EventKey?.sidebar(regex: String): SidebarLineDelegate {
    return SidebarLineDelegate(this, regex)
}