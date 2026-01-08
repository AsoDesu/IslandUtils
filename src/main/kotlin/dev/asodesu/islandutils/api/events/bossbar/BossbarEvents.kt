package dev.asodesu.islandutils.api.events.bossbar

import dev.asodesu.islandutils.api.events.arrayBackedEvent

object BossbarEvents {

    val BOSSBAR_CONTENTS_UPDATE = arrayBackedEvent { callbacks ->
        BossbarContentsUpdate { uuid, component ->
            callbacks.forEach { it.onBossbarContents(uuid, component) }
        }
    }

}