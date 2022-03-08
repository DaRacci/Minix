package dev.racci.minix.api.coroutine.contract

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface CoroutineSession {

    /**
     * Gets the scope.
     */
    val scope: CoroutineScope

    /**
     * Gets the event service.
     */
    val eventService: EventService

    /**
     * Gets the command service.
     */
    val commandService: CommandService

    /**
     * Gets the wakeup service.
     */
    val wakeUpBlockService: WakeUpBlockService

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
        f: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * Disposes the session.
     */
    fun dispose()
}
