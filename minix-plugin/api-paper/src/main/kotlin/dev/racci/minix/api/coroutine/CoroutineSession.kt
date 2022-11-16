package dev.racci.minix.api.coroutine

import dev.racci.minix.api.data.enums.EventExecutionType
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import org.apiguardian.api.API
import org.bukkit.event.Event
import org.bukkit.event.Listener
import kotlin.coroutines.CoroutineContext

public actual interface CoroutineSession : WithPlugin<MinixPlugin> {
    /**
     * Gets the scope.
     */
    actual override val coroutineScope: CoroutineScope

    /**
     * Asynchronous context that stays off the main thread.
     */
    actual override val context: CoroutineContext

    /** Synchronous context that stays on the main bukkit thread. */
    actual override val minecraftContext: CoroutineContext

    @API(status = API.Status.INTERNAL)
    public actual fun withManipulatedServerHeartBeat(
        block: suspend CoroutineScope.() -> Unit
    )

    /**
     * Launches the given function on the plugin coroutineService scope.
     * @return Cancelable coroutineService job.
     */
    public actual fun launch(
        context: CoroutineContext,
        parentScope: CoroutineScope,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * Disposes the session.
     */
    public actual fun dispose()

    public suspend fun registerSuspendedListener(
        listener: Listener
    )

    public fun fireSuspendingEvent(
        event: Event,
        executionType: EventExecutionType
    ): Collection<Job>
}
