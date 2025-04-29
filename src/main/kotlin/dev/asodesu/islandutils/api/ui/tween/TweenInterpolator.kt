package dev.asodesu.islandutils.api.ui.tween

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

interface TweenInterpolator<T> {
    fun lerp(t: Float, a: T, b: T): T

    object IntInterpolator : TweenInterpolator<Int> {
        override fun lerp(t: Float, a: Int, b: Int) = Mth.lerp(t, a.toFloat(), b.toFloat()).toInt()
    }

    object FloatInterpolator : TweenInterpolator<Float> {
        override fun lerp(t: Float, a: Float, b: Float) = Mth.lerp(t, a, b)
    }

    object DoubleInterpolator : TweenInterpolator<Double> {
        override fun lerp(t: Float, a: Double, b: Double) = Mth.lerp(t.toDouble(), a, b)
    }

    object Vec3Interpolator : TweenInterpolator<Vec3> {
        override fun lerp(t: Float, a: Vec3, b: Vec3): Vec3 = Mth.lerp(t.toDouble(), a, b)
    }
}