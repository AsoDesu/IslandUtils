package dev.asodesu.islandutils.api.game

import dev.asodesu.islandutils.api.events.EventConsumer
import dev.asodesu.islandutils.api.events.SingleEventConsumerDelegate

class ActiveGameEventHandler(private val manager: GameManager) : SingleEventConsumerDelegate {
    override val consumer: EventConsumer get() = manager.active
}