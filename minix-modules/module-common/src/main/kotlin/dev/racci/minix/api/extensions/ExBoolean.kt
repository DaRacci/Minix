package dev.racci.minix.api.extensions

/**
 * Returns this boolean as an integer.
 *
 * @return 1 if true, 0 if false, -1 if null.
 */
public fun Boolean?.asInt(): Int = when (this) {
    null -> -1
    true -> 1
    false -> 0
}

/**
 * Returns this boolean as an integer.
 *
 * @return 1 if true, 0 if false.
 */
public fun Boolean.asInt(): Int = if (this) 1 else 0

/**
 * Converts an integer into a boolean.
 *
 * @return true if the integer is 1, false if 0.
 */
public fun Int.asBoolean(): Boolean = when (this) {
    0 -> false
    1 -> true
    else -> error("Value $this is not a valid boolean value, must be 0 or 1")
}
