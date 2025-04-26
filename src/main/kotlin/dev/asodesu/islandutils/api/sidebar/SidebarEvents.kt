package dev.asodesu.islandutils.api.sidebar

import dev.asodesu.islandutils.api.events.MutableListEvent

object SidebarEvents {

    val LINE_UPDATE = MutableListEvent<SidebarLineUpdate> { callbacks ->
        SidebarLineUpdate { component ->
            callbacks.forEach { it.onSidebarLine(component) }
        }
    }

}