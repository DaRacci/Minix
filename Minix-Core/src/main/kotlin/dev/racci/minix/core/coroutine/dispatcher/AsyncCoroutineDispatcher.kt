package dev.racci.minix.core.coroutine.dispatcher

import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

internal class AsyncCoroutineDispatcher(
    private val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {

    /**
     * Returns `true` if the execution of the coroutine should be performed with [dispatch] method.
     * The default behavior for most dispatchers is to return `true`.
     * This method should generally be exception-safe.
     * An exception thrown from this method may leave the coroutines that use this dispatcher in the inconsistent and hard to debug state.
     */
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return plugin.server.isPrimaryThread
    }

    /**
     * Handles dispatching the coroutine on the correct thread.
     */
    override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    ) {
        when {
            !plugin.isEnabled -> return
            server.isPrimaryThread -> {
                plugin.server.scheduler.runTaskAsynchronously(plugin, block)
                wakeUpBlockService.ensureWakeup()
            }
            else -> block.run()
        }
    }
}
