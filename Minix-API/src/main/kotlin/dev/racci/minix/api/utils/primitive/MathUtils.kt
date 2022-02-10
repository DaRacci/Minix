@file:Suppress("UNUSED")

package dev.racci.minix.api.utils.primitive

object MathUtils {

    fun multiplyByPercent(
        value: Double,
        percent: Double,
    ): Double {
        return (value * (percent / 100))
    }

    fun getMultiplierFromPercent(percent: Double): Double {
        return (percent / 100)
    }
}
