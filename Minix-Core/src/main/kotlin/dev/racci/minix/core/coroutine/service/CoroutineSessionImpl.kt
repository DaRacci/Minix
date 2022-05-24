package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.coroutine.contract.CommandService
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.contract.EventService
import dev.racci.minix.api.coroutine.contract.WakeUpBlockService
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extensions.WithPlugin
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

internal class CoroutineSessionImpl(override val plugin: MinixPlugin) : WithPlugin<MinixPlugin>, CoroutineSession {

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
        parentScope: CoroutineScope?,
        f: suspend CoroutineScope.() -> Unit,
    ): Job = when {
        disposed -> Job()
        dispatcher == Dispatchers.Unconfined -> launchInternal(parentScope, dispatcher, CoroutineStart.UNDISPATCHED, f) // If the dispatcher is unconfined. Always schedule immediately.
        else -> launchInternal(parentScope, dispatcher, CoroutineStart.DEFAULT, f)
    }

    private fun launchInternal(
        parentScope: CoroutineScope?,
        dispatcher: CoroutineContext,
        coroutineStart: CoroutineStart,
        f: suspend CoroutineScope.() -> Unit,
    ): Job = (parentScope ?: scope).launch(dispatcher, coroutineStart) {
        try { // The user may or may not launch multiple sub suspension operations. If
            // one of those fails, only this scope should fail instead of the parent scope.
            coroutineScope(f)
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> plugin.log.info { "Coroutine cancelled: ${e.message}" }
                else -> plugin.log.error(e) { "Unhandled Exception from coroutine." }
            }
        }
    }
}
