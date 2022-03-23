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
import kotlin.coroutines.CoroutineContext

internal class CoroutineSessionImpl(private val plugin: MinixPlugin) : CoroutineSession {

    private var disposed = false

    override val scope: CoroutineScope by lazy { CoroutineScope(plugin.minecraftDispatcher) }
    override val eventService: EventService by lazy { EventServiceImpl(plugin, this) }
    override val commandService: CommandService by lazy { CommandServiceImpl(plugin, this) }
    override val wakeUpBlockService: WakeUpBlockService by lazy { WakeUpBlockServiceImpl(plugin) }
    override val dispatcherMinecraft: CoroutineContext by lazy { MinecraftCoroutineDispatcher(plugin, wakeUpBlockService) }
    override val dispatcherAsync: CoroutineContext by lazy { AsyncCoroutineDispatcher(plugin, wakeUpBlockService) }

    override fun dispose() {
        disposed = true
        scope.coroutineContext.cancelChildren()
        wakeUpBlockService.dispose()
    }

    override fun launch(
        dispatcher: CoroutineContext,
        f: suspend CoroutineScope.() -> Unit,
    ) = when {
        disposed -> Job()
        dispatcher == Dispatchers.Unconfined -> launchInternal(dispatcher, CoroutineStart.UNDISPATCHED, f) // If the dispatcher is unconfined. Always schedule immediately.
        else -> launchInternal(dispatcher, CoroutineStart.DEFAULT, f)
    }

    private fun launchInternal(
        dispatcher: CoroutineContext,
        coroutineStart: CoroutineStart,
        f: suspend CoroutineScope.() -> Unit,
    ): Job = scope.launch(dispatcher, coroutineStart) {
        try { // The user may or may not launch multiple sub suspension operations. If
            // one of those fails, only this scope should fail instead of the plugin scope.
            coroutineScope {
                f.invoke(this)
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> plugin.log.info { "Coroutine cancelled: ${e.message}" }
                else -> plugin.log.error(e) { "Unhandled Exception from coroutine." }
            }
        }
    }
}
