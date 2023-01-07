package dev.racci.minix.api.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import org.apiguardian.api.API
import kotlin.coroutines.CoroutineContext

public expect interface CoroutineSession {

    /**
     * Gets the scope.
     */
    public val scope: CoroutineScope

    /**
     * The context for the plugin.
     * Check the actual implementation for more information for that platform.
     */
    public val context: CoroutineContext

    @API(status = API.Status.INTERNAL)
    public fun withManipulatedServerHeartBeat(
        block: suspend CoroutineScope.() -> Unit
    )

    /**
     * Launches the given function on the plugin coroutineService scope.
     * @return Cancelable coroutineService job.
     */
    public fun launch(
        context: CoroutineContext = this.context,
        scope: CoroutineScope = this.scope,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * Disposes the session.
     */
    public fun dispose()
}
