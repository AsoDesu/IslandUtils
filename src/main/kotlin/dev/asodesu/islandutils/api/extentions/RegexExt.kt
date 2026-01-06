package dev.asodesu.islandutils.api.extentions

import net.minecraft.network.chat.Component

fun Regex.withValue(component: Component, callback: (String) -> Unit) {
    val plainString = component.string
    val result = this.find(plainString) ?: return
    val value = result.groupValues.getOrNull(1) ?: return
    callback(value)
}