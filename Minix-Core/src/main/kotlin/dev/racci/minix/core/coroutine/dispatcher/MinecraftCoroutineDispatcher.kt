package dev.racci.minix.core.coroutine.dispatcher

import arrow.core.toOption
import dev.racci.minix.api.coroutine.CoroutineTimings
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.core.coroutine.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

internal class MinecraftCoroutineDispatcher(
    private val plugin: Plugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {

    /**
     * Returns `true` if the execution of the coroutine should be performed with [dispatch] method.
     * The default behavior for most dispatchers is to return `true`.
     * This method should generally be exception-safe.
     * An exception thrown from this method
     * may leave the coroutines that use this dispatcher in the inconsistent and hard to debug state.
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
            println("Plugin isn't enabled, not dispatching")
            return
        }

        context[CoroutineTimings.Key].toOption()
            .tapNone { scheduler.runTask(plugin, block) }
            .tap { timed -> timed.queue.add(block) }
            .tap { timed -> scheduler.runTaskAsynchronously(plugin, timed) }
    }
}
