package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import kotlin.reflect.KCallable

/** @see [AccessUtils.accessWith] */
public suspend inline fun <A, C : KCallable<*>> C.accessWith(
    action: C.() -> A
): A = AccessUtils.accessWith(this, action)
