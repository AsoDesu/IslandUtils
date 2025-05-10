package dev.asodesu.islandutils.api.ui.tween

import kotlin.math.pow

fun interface Easing {
    fun transform(x: Float): Float

    companion object {
        val EASE_OUT_EXPO = Easing { if (it == 1f) 1f else 1 - 2f.pow(-10f * it) }
    }
}