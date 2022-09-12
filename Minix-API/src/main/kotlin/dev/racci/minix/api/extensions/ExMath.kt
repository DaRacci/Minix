package dev.racci.minix.api.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.round(decimals: Int) = BigDecimal(this).setScale(decimals, RoundingMode.HALF_EVEN).toDouble()
