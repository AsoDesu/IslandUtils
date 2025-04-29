package dev.asodesu.islandutils.api.ui.tween

import dev.asodesu.islandutils.api.ticks
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.time.Duration

class Tween<T>(
    private val from: T,
    private val to: T,
    duration: Duration,
    private val interpolator: TweenInterpolator<T>,
    private val easing: Easing? = null,
) {
    private val durationTicks = duration.ticks
    private var elapsed = 0f
    var finished = false

    fun tick(tickDelta: Float): T {
        elapsed += tickDelta
        val elapsedT = Mth.clamp(elapsed / durationTicks, 0f, 1f)
        val eased = easing?.transform(elapsedT) ?: elapsedT

        finished = elapsed >= durationTicks
        return interpolator.lerp(eased, from, to)
    }

    companion object {
        fun int(from: Int, to: Int, duration: Duration, easing: Easing? = null) = Tween(from, to, duration, TweenInterpolator.IntInterpolator, easing)
        fun float(from: Float, to: Float, duration: Duration, easing: Easing? = null) = Tween(from, to, duration, TweenInterpolator.FloatInterpolator, easing)
        fun double(from: Double, to: Double, duration: Duration, easing: Easing? = null) = Tween(from, to, duration, TweenInterpolator.DoubleInterpolator, easing)
        fun vec3(from: Vec3, to: Vec3, duration: Duration, easing: Easing? = null) = Tween(from, to, duration, TweenInterpolator.Vec3Interpolator, easing)
    }

}