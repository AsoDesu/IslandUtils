package dev.asodesu.islandutils.api.events.sidebar

import dev.asodesu.islandutils.api.events.arrayBackedEvent

object SidebarEvents {

    val LINE_UPDATE = arrayBackedEvent { callbacks ->
        SidebarLineUpdate { component ->
            callbacks.forEach { it.onSidebarLine(component) }
        }
    }

}