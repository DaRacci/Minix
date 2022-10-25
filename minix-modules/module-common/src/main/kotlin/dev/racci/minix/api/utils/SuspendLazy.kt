package dev.racci.minix.api.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

public fun <T> suspendBlockingLazy(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    initializer: suspend () -> T
): SuspendLazy<T> = SuspendLazyBlockingImpl(dispatcher, initializer)

public fun <T> CoroutineScope.suspendLazy(
    context: CoroutineContext = EmptyCoroutineContext,
    initializer: suspend CoroutineScope.() -> T
): SuspendLazy<T> = SuspendLazySuspendingImpl(this, context, initializer)

public interface SuspendLazy<out T> : Lazy<T> {
    public suspend operator fun invoke(): T
}

private class SuspendLazyBlockingImpl<out T>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    initializer: suspend () -> T
) : SuspendLazy<T> {
    private val lazyValue = lazy { runBlocking { initializer() } }

    override val value: T get() = runBlocking { invoke() }

    override fun isInitialized(): Boolean = lazyValue.isInitialized()

    override suspend operator fun invoke(): T = with(lazyValue) {
        if (isInitialized()) value else withContext(dispatcher) { value }
    }
}

private class SuspendLazySuspendingImpl<out T>(
    coroutineScope: CoroutineScope,
    context: CoroutineContext,
    initializer: suspend CoroutineScope.() -> T
) : SuspendLazy<T> {
    private val deferred by lazy { coroutineScope.async(context, start = LAZY, block = initializer) }
    override suspend operator fun invoke(): T = deferred.await()

    override val value: T get() = runBlocking { invoke() }

    override fun isInitialized(): Boolean = deferred.isCompleted
}
