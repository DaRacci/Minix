package dev.racci.minix.data.extensions

@JvmName("toDoubleIntInt")
public fun Pair<Int, Int>.toDouble(): Pair<Double, Double> = first.toDouble() to second.toDouble()

@JvmName("toFloatIntInt")
public fun Pair<Int, Int>.toFloat(): Pair<Float, Float> = first.toFloat() to second.toFloat()

@JvmName("toIntDoubleDouble")
public fun Pair<Double, Double>.toInt(): Pair<Int, Int> = first.toInt() to second.toInt()

@JvmName("toFloatDoubleDouble")
public fun Pair<Double, Double>.toFloat(): Pair<Float, Float> = first.toFloat() to second.toFloat()
