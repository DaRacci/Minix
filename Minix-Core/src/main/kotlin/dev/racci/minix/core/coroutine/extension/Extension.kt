package dev.racci.minix.core.coroutine.extension

import org.jetbrains.annotations.ApiStatus
import java.lang.reflect.Method

/**
 * Internal reflection suspend.
 */
@ApiStatus.Internal
internal suspend fun Method.invokeSuspend(
    obj: Any,
    vararg args: Any?
): Any? = kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn { cont ->
    invoke(obj, *args, cont)
}
