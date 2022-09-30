package dev.racci.minix.api.utils.primitive

import dev.racci.minix.api.utils.UtilObject

object MathUtils : UtilObject {

    fun multiplyByPercent(
        value: Double,
        percent: Double
    ): Double {
        return (value * (percent / 100))
    }

    fun getMultiplierFromPercent(percent: Double): Double {
        return (percent / 100)
    }
}
