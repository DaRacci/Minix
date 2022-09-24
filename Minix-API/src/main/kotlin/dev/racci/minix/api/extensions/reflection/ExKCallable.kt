package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import org.checkerframework.checker.units.qual.A
import kotlin.reflect.KCallable

/** @see [AccessUtils.accessWith] */
inline fun <R, C : KCallable<*>> C.accessWith(
    action: C.() -> A
): A = AccessUtils.accessWith(this, action)
