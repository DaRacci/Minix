package dev.racci.minix.core.coroutine.dispatcher

import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

internal expect class MinixCoroutineDispatcher : CoroutineDispatcher {
    @Suppress("ProtectedInFinal")
    protected val plugin: MinixPlugin

    /**
     * Returns `true` if the execution of the coroutine should be performed with [dispatch] method.
     * The default behavior for most dispatchers is to return `true`.
     * This method should generally be exception-safe.
     * An exception thrown from this method may leave the coroutines that use this context in the inconsistent and hard to debug state.
     */
    override fun isDispatchNeeded(context: CoroutineContext): Boolean

    /**
     * Handles dispatching the coroutine on the correct thread.
     */
    override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    )
}
