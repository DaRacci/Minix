package dev.racci.minix.core.coroutine.dispatcher

import dev.racci.minix.api.coroutine.contract.WakeUpBlockService
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

internal class AsyncCoroutineDispatcher(
    private val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {

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
