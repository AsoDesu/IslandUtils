package dev.asodesu.islandutils.api.events.bossbar

import java.util.*
import net.minecraft.network.chat.Component

fun interface BossbarContentsUpdate {
    fun onBossbarContents(uuid: UUID, contents: Component)
}