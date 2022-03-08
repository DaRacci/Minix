package dev.racci.minix.core.coroutine.dispatcher

import dev.racci.minix.api.coroutine.contract.WakeUpBlockService
import dev.racci.minix.api.extensions.server
import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

internal class MinecraftCoroutineDispatcher(
    private val plugin: Plugin,
    private val wakeUpBlockService: WakeUpBlockService,
) : CoroutineDispatcher() {

    /**
     * Handles dispatching the coroutine on the correct thread.
     */
    override fun dispatch(
        context: CoroutineContext,
        block: Runnable,
    ) {
        when {
            !plugin.isEnabled -> return
            server.isPrimaryThread -> block.run()
            else -> {
                server.scheduler.runTask(plugin, block)
                wakeUpBlockService.ensureWakeup()
            }
        }
    }
}
