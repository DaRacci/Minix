package dev.racci.minix.core.coroutine.dispatcher

import arrow.core.toOption
import dev.racci.minix.api.coroutine.CoroutineTimings
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.service.WakeUpBlockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

internal class MinecraftCoroutineDispatcher(
    private val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : ExecutorCoroutineDispatcher() {
    override val executor: Executor
        get() = Dispatchers.bukkit.executor

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = Dispatchers.Main.isDispatchNeeded(context)

    /**
     * Handles dispatching the coroutine on the correct thread.
     */
    override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    ) {
        context[CoroutineTimings.Key].toOption()
            .tapNone { Dispatchers.Main.dispatch(context, block) }
            .tap { timed -> timed.queue.add(block) }
            .tap { timed -> Dispatchers.Main.dispatch(context, timed) }
    }

    override fun close() {
        return // Do nothing
    }
}
