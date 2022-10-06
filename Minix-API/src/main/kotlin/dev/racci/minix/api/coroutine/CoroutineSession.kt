package dev.racci.minix.api.coroutine

import dev.racci.minix.api.data.enums.EventExecutionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import org.bukkit.event.Event
import org.bukkit.event.Listener
import kotlin.coroutines.CoroutineContext

interface CoroutineSession {

    /**
     * Gets the scope.
     */
    val scope: CoroutineScope

    /**
     * Gets the minecraft dispatcher.
     */
    val minecraftDispatcher: CoroutineContext

    /**
     * Gets the async dispatcher.
     */
    val asyncDispatcher: CoroutineContext

    var isManipulatedServerHeartBeat: Boolean

    suspend fun registerSuspendedListener(listener: Listener)

    fun fireSuspendingEvent(
        event: Event,
        executionType: EventExecutionType
    ): Collection<Job>

    /**
     * Launches the given function on the plugin coroutineService scope.
     * @return Cancelable coroutineService job.
     */
    fun launch(
        dispatcher: CoroutineContext,
        parentScope: CoroutineScope = scope,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * Disposes the session.
     */
    fun dispose()
}
