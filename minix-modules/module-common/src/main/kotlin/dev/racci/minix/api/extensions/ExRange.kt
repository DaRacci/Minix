package dev.racci.minix.api.extensions // ktlint-disable filename

import kotlin.random.Random

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
public fun IntRange.randomOrMin(): Int =
    if (start >= endInclusive) {
        start
    } else Random.nextInt(start, endInclusive)

/**
 * A random value from this range's min to max, or the minimum value if max is smaller or equal to min.
 */
public fun LongRange.randomOrMin(): Long =
    if (start >= endInclusive) {
        start
    } else Random.nextLong(start, endInclusive)
