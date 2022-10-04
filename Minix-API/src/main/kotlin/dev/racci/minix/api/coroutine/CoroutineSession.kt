package dev.racci.minix.api.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface CoroutineSession {

    /**
     * Gets the scope.
     */
    val scope: CoroutineScope

    /**
     * Gets the minecraft dispatcher.
     */
    val dispatcherMinecraft: CoroutineContext

    /**
     * Gets the async dispatcher.
     */
    val dispatcherAsync: CoroutineContext

    /**
     * Launches the given function on the plugin coroutineService scope.
     * @return Cancelable coroutineService job.
     */
    fun launch(
        dispatcher: CoroutineContext,
        parentScope: CoroutineScope?,
        f: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * Disposes the session.
     */
    fun dispose()
}
