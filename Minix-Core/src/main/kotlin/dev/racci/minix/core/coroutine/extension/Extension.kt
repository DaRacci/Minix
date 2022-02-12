package dev.racci.minix.core.coroutine.extension

import java.lang.reflect.Method

/**
 * Internal reflection suspend.
 */
internal suspend fun Method.invokeSuspend(
    obj: Any,
    vararg args: Any?,
): Any? =
    kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *args, cont)
    }
