package dev.racci.minix.api.extensions

import arrow.core.Option
import arrow.core.getOrElse

public fun Option<Boolean>.orFalse(): Boolean = this.getOrElse { false }

public fun Option<Boolean>.orTrue(): Boolean = this.getOrElse { true }
