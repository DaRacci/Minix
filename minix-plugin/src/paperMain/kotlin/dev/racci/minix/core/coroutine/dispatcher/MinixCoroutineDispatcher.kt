package dev.racci.minix.core.coroutine.dispatcher

import arrow.core.getOrElse
import arrow.core.toOption
import dev.racci.minix.api.coroutine.CoroutineTimings
import dev.racci.minix.api.extensions.collections.findKProperty
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.service.WakeUpBlockService
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.bukkit.craftbukkit.v1_19_R2.scheduler.CraftAsyncScheduler
import org.bukkit.craftbukkit.v1_19_R2.scheduler.CraftScheduler
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.memberProperties

internal actual class MinixCoroutineDispatcher(
    @Suppress("ProtectedInFinal") protected actual val plugin: MinixPlugin,
    private val wakeUpBlockService: WakeUpBlockService
) : ExecutorCoroutineDispatcher(), KoinComponent {
    override val executor = runBlocking {
        val asyncScheduler = CraftScheduler::class.memberProperties.findKProperty<CraftAsyncScheduler>("asyncScheduler")
            .map { it.accessGet(server.scheduler) }
            .getOrElse { throw IllegalStateException("Could not find async scheduler.") }

        asyncScheduler::class.memberProperties.findKProperty<Executor>("executor")
            .map { it.accessGet(asyncScheduler) }
            .getOrElse { throw IllegalStateException("Could not find executor.") }
    }

    actual override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        wakeUpBlockService.ensureWakeup()
        return plugin.server.isPrimaryThread
    }

    actual override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    ) {
        context[CoroutineTimings.Key].toOption()
            .tapNone { executor.execute(block) }
            .tap { timed -> timed.queue.add(block) }
            .tap { timed -> executor.execute(timed) }
    }

    override fun close() {
        return // We don't need to close this.
    }
}
