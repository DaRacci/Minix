package dev.racci.minix.api.extensions // ktlint-disable filename

import java.math.BigDecimal
import java.math.RoundingMode

public fun Double.round(decimals: Int): Double = BigDecimal(this).setScale(decimals, RoundingMode.HALF_EVEN).toDouble()
