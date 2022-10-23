package dev.racci.minix.api.extensions // ktlint-disable filename

import dev.racci.minix.api.utils.primitive.EnumUtils

public fun Enum<*>.formatted(
    separator: String = " ",
    capitalize: Boolean = true
): String = EnumUtils.formatted(this, separator, capitalize)
