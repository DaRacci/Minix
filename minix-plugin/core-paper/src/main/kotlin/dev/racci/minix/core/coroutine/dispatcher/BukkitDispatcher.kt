package dev.racci.minix.core.coroutine.dispatcher

import arrow.core.Either
import arrow.core.Option
import com.google.common.util.concurrent.AbstractListeningExecutorService
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.utils.kotlin.catch
import dev.racci.minix.core.coroutine.dispatcher.Bukkit.delay
import dev.racci.minix.flowbus.dispatcher.DispatcherFactory
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelFutureOnCancellation
import kotlinx.coroutines.internal.MainDispatcherFactory
import org.bukkit.craftbukkit.v1_19_R2.CraftServer
import java.util.concurrent.Future
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@Suppress("UnusedReceiverParameter")
public val Dispatchers.bukkit: BukkitDispatcher
    get() = Bukkit

@OptIn(InternalCoroutinesApi::class)
public sealed class BukkitDispatcher : MainCoroutineDispatcher(), Delay {
    @Suppress("UnstableApiUsage")
    private val executor: AbstractListeningExecutorService = Option.catch { server as CraftServer }
        .map { it.server.executor.castOrThrow<AbstractListeningExecutorService>() }.orNull() ?: throw IllegalStateException("Could not find server.")

    override fun dispatch(
        context: CoroutineContext,
        block: Runnable
    ) {
        Either.catch<RejectedExecutionException, Unit> { executor.execute(block) }
            .tapLeft { err -> cancelJobOnRejection(context, err) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(
        timeMillis: Long,
        continuation: CancellableContinuation<Unit>
    ) {
        val future = (executor as ScheduledExecutorService).scheduleBlock(
            { with(continuation) { resumeUndispatched(Unit) } },
            continuation.context,
            timeMillis
        )

        if (future != null) {
            continuation.cancelFutureOnCancellation(future)
        }
    }

    override fun invokeOnTimeout(
        timeMillis: Long,
        block: Runnable,
        context: CoroutineContext
    ): DisposableHandle {
        val future = (executor as ScheduledExecutorService).scheduleBlock(block, context, timeMillis)
        return when {
            future != null -> DisposableFutureHandle(future)
            else -> DisposableHandle { }
        }
    }

    private fun ScheduledExecutorService.scheduleBlock(
        block: Runnable,
        context: CoroutineContext,
        timeMillis: Long
    ): ScheduledFuture<*>? = try {
        schedule(block, timeMillis, TimeUnit.MILLISECONDS)
    } catch (e: RejectedExecutionException) {
        cancelJobOnRejection(context, e)
        null
    }

    private fun cancelJobOnRejection(
        context: CoroutineContext,
        exception: RejectedExecutionException
    ): Unit = context.cancel(CancellationException("The task was rejected", exception))
}

@OptIn(InternalCoroutinesApi::class)
public object BukkitDispatcherFactory : MainDispatcherFactory, DispatcherFactory {
    override val loadPriority: Int get() = 2

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher = Bukkit
    override fun getDispatcher(): CoroutineDispatcher = Bukkit
}

private object ImmediateBukkitDispatcher : BukkitDispatcher() {
    override val immediate: MainCoroutineDispatcher get() = this

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = server.isPrimaryThread

    @OptIn(InternalCoroutinesApi::class)
    override fun toString() = toStringInternalImpl() ?: "Bukkit.immediate"
}

private class DisposableFutureHandle(private val future: Future<*>) : DisposableHandle {
    override fun dispose() {
        future.cancel(false)
    }

    override fun toString(): String = "DisposableFutureHandle[$future]"
}

/**
 * Dispatches execution onto the bukkit main thread and provides native [delay] support.
 */
internal object Bukkit : BukkitDispatcher() {

    override val immediate: MainCoroutineDispatcher
        get() = ImmediateBukkitDispatcher

    @OptIn(InternalCoroutinesApi::class)
    override fun toString() = toStringInternalImpl() ?: "Bukkit"
}
