package dev.asodesu.islandutils.api.events

import net.fabricmc.fabric.api.event.EventFactory

inline fun <reified T : Any> arrayBackedEvent(noinline invoker: (Array<T>) -> T) =
    EventFactory.createArrayBacked(T::class.java, invoker)

fun MultiEventConsumer(children: () -> List<EventConsumer>) = object : MultiEventConsumer {
    override fun children() = children()
}