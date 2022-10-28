package dev.racci.minix.core.coroutine.dispatcher

import arrow.core.toOption
import dev.racci.minix.api.coroutine.CoroutineTimings
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

internal class MinecraftCoroutineDispatcher(
    private val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {
    private val logger by MinixLoggerFactory

    /**
     * Returns `true` if the execution of the coroutine should be performed with [dispatch] method.
     * The default behavior for most dispatchers is to return `true`.
     * This method should generally be exception-safe.
     * An exception thrown from this method
     * may leave the coroutines that use this context in the inconsistent and hard to debug state.
     */
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return !plugin.server.isPrimaryThread
    }

    /**
     * Handles dispatching the coroutine on the correct thread.
     */
    override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    ) {
        if (!plugin.isEnabled) {
            logger.warn { "Plugin isn't enabled, not dispatching" }
            return
        }

        context[CoroutineTimings.Key].toOption()
            .tapNone { scheduler.runTask(plugin, block) }
            .tap { timed -> timed.queue.add(block) }
            .tap { timed -> scheduler.runTaskAsynchronously(plugin, timed) }
    }
}
