package dev.racci.minix.core.coroutine.dispatcher

import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.service.WakeUpBlockService
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

internal actual class MinixCoroutineDispatcher(
    protected actual val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : CoroutineDispatcher() {

    actual override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return plugin.server.isPrimaryThread
    }

    actual override fun dispatch(
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
