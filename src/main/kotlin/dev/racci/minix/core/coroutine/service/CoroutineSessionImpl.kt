package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.coroutine.contract.CommandService
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.contract.EventService
import dev.racci.minix.api.coroutine.contract.WakeUpBlockService
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.AsyncCoroutineDispatcher
import dev.racci.minix.core.coroutine.dispatcher.MinecraftCoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Level
import kotlin.coroutines.CoroutineContext

internal class CoroutineSessionImpl(private val plugin: MinixPlugin) : CoroutineSession {

    private var disposed = false

    /**
     * Gets the scope.
     */
    override val scope: CoroutineScope by lazy {
        CoroutineScope(plugin.minecraftDispatcher)
    }

    /**
     * Gets the event service.
     */
    override val eventService: EventService by lazy {
        EventServiceImpl(plugin, this)
    }

    /**
     * Gets the command service.
     */
    override val commandService: CommandService by lazy {
        CommandServiceImpl(plugin, this)
    }

    /**
     * Gets the wakeup service.
     */
    override val wakeUpBlockService: WakeUpBlockService by lazy {
        WakeUpBlockServiceImpl(plugin)
    }

    /**
     * Gets the minecraft dispatcher.
     */
    override val dispatcherMinecraft: CoroutineContext by lazy {
        MinecraftCoroutineDispatcher(plugin, wakeUpBlockService)
    }

    /**
     * Gets the async dispatcher.
     */
    override val dispatcherAsync: CoroutineContext by lazy {
        AsyncCoroutineDispatcher(plugin, wakeUpBlockService)
    }

    /**
     * Disposes the session.
     */
    override fun dispose() {
        disposed = true
        scope.coroutineContext.cancelChildren()
        wakeUpBlockService.dispose()
    }

    /**
     * Launches the given function on the plugin coroutine scope.
     */
    override fun launch(
        dispatcher: CoroutineContext,
        f: suspend CoroutineScope.() -> Unit,
    ): Job {
        if (disposed) {
            return Job()
        }

        if (dispatcher == Dispatchers.Unconfined) {
            // If the dispatcher is unconfined. Always schedule immediately.
            return launchInternal(dispatcher, CoroutineStart.UNDISPATCHED, f)
        }

        return launchInternal(dispatcher, CoroutineStart.DEFAULT, f)
    }

    private fun launchInternal(
        dispatcher: CoroutineContext,
        coroutineStart: CoroutineStart,
        f: suspend CoroutineScope.() -> Unit,
    ): Job = scope.launch(dispatcher, coroutineStart) {
        try {
            // The user may or may not launch multiple sub suspension operations. If
            // one of those fails, only this scope should fail instead of the plugin scope.
            coroutineScope {
                f.invoke(this)
            }
        } catch (e: CancellationException) {
            plugin.logger.log(Level.INFO, "Coroutine has been cancelled.")
        }
    }
}
