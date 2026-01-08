package dev.asodesu.islandutils.api.events

import net.fabricmc.fabric.api.event.EventFactory

inline fun <reified T : Any> arrayBackedEvent(noinline invoker: (Array<T>) -> T) =
    EventFactory.createArrayBacked(T::class.java, invoker)

fun EventConsumerWrapper(consumer: () -> EventConsumer) = object : EventConsumerWrapper {
    override val consumer: EventConsumer get() = consumer()
}
fun MultiEventConsumerWrapper(children: () -> List<EventConsumer>) = object : MultiEventConsumerWrapper {
    override fun children() = children()
}