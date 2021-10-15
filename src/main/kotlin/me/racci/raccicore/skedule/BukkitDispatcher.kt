package me.racci.raccicore.skedule

import kotlinx.coroutines.*
import me.racci.raccicore.RacciPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.CoroutineContext

internal val bukkitScheduler
    get() = Bukkit.getScheduler()


@OptIn(InternalCoroutinesApi::class)
class BukkitDispatcher(val plugin: RacciPlugin, val async: Boolean = false) : CoroutineDispatcher(), Delay {

    private val runTaskLater: (RacciPlugin, Runnable, Long) -> BukkitTask
        get() = if (async) {
            bukkitScheduler::runTaskLaterAsynchronously
        } else {
            bukkitScheduler::runTaskLater
        }

    private val runTask: (RacciPlugin, Runnable) -> BukkitTask
        get() = if (async) {
            bukkitScheduler::runTaskAsynchronously
        } else {
            bukkitScheduler::runTask
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = runTaskLater(
            plugin,
            Runnable {
                continuation.apply { resumeUndispatched(Unit) }
            },
            timeMillis / 50)
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) {
            return
        }

        if (!async && Bukkit.isPrimaryThread()) {
            block.run()
        } else {
            runTask(plugin, block)
        }
    }

}

fun RacciPlugin.dispatcher(async: Boolean = false) = BukkitDispatcher(this, async)