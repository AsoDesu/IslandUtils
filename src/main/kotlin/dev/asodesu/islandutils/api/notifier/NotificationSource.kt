package dev.asodesu.islandutils.api.notifier

import net.minecraft.network.chat.MutableComponent

interface NotificationSource {
    val title: MutableComponent
    fun provide(): List<Notification>
}