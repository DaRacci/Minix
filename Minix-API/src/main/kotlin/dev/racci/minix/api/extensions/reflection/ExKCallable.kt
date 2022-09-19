package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import kotlin.reflect.KCallable

/** @see [AccessUtils.accessWith] */
inline fun <R, A, C : KCallable<R>> C.accessWith(
    action: C.() -> A
): A = AccessUtils.accessWith(this, action)
