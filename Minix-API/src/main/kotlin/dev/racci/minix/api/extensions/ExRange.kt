package dev.racci.minix.api.extensions

import dev.racci.minix.api.aliases.DoubleRange
import dev.racci.minix.api.aliases.FloatRange
import kotlin.random.Random

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
fun DoubleRange.randomOrMin(): Double =
    if (start >= endInclusive) {
        start
    } else Random.nextDouble(start, endInclusive)

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
fun FloatRange.randomOrMin(): Float =
    if (start >= endInclusive) {
        start
    } else start + Random.nextFloat() * (endInclusive - start)

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
fun IntRange.randomOrMin(): Int =
    if (start >= endInclusive) {
        start
    } else Random.nextInt(start, endInclusive)

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
fun LongRange.randomOrMin(): Long =
    if (start >= endInclusive) {
        start
    } else Random.nextLong(start, endInclusive)
