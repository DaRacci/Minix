package dev.racci.minix.api.utils.primitive

public object MathUtils {

    public fun multiplyByPercent(
        value: Double,
        percent: Double
    ): Double {
        return (value * (percent / 100))
    }

    public fun getMultiplierFromPercent(percent: Double): Double {
        return (percent / 100)
    }
}
