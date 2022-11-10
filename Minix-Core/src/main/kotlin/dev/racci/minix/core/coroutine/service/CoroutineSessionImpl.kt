package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.data.enums.EventExecutionType
import dev.racci.minix.api.events.plugin.CaughtCoroutineExceptionEvent
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.dispatcher.AsyncCoroutineDispatcher
import dev.racci.minix.core.coroutine.dispatcher.MinecraftCoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.bukkit.event.Event
import org.bukkit.event.Listener
import kotlin.coroutines.CoroutineContext

internal class CoroutineSessionImpl(override val plugin: MinixPlugin) : WithPlugin<MinixPlugin>, CoroutineSession {
    private val wakeUpBlockService by lazy { WakeUpBlockService(plugin) }
    private val eventService by lazy { EventService(plugin, this) }

    override val minecraftDispatcher by lazy { MinecraftCoroutineDispatcher(plugin, wakeUpBlockService) }
    override val asyncDispatcher by lazy { AsyncCoroutineDispatcher(plugin, wakeUpBlockService) }

    override val scope: CoroutineScope = CoroutineExceptionHandler { _, err ->
        if (!plugin.isEnabled) return@CoroutineExceptionHandler
        async {
            val event = CaughtCoroutineExceptionEvent(plugin, err)
            if (!event.callEvent() || err is CancellationException) return@async

            logger.fatal(err) { "There was an uncaught exception in a launched coroutine." }
        }
    }.let(::CoroutineScope) + SupervisorJob() + asyncDispatcher

    override var isManipulatedServerHeartBeat: Boolean
        get() = wakeUpBlockService.isManipulatedServerHeartBeatEnabled
        set(value) { wakeUpBlockService.isManipulatedServerHeartBeatEnabled = value }

    override suspend fun registerSuspendedListener(listener: Listener) = eventService.registerSuspendListener(listener)
    override fun fireSuspendingEvent(
        event: Event,
        executionType: EventExecutionType
    ): Collection<Job> = eventService.fireSuspendingEvent(event) // , executionType)

    override fun launch(
        dispatcher: CoroutineContext,
        parentScope: CoroutineScope,
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ): Job = when {
        !parentScope.isActive -> Job()
        dispatcher == Dispatchers.Unconfined -> launchInternal(parentScope, dispatcher, CoroutineStart.UNDISPATCHED, block) // If the dispatcher is unconfined. Always schedule immediately.
        else -> launchInternal(parentScope, dispatcher, start, block)
    }

    override fun dispose() {
        scope.coroutineContext.cancelChildren()
        scope.cancel()

        wakeUpBlockService.dispose()
        minecraftDispatcher.cancel()
        asyncDispatcher.cancel()
    }

    private fun launchInternal(
        parentScope: CoroutineScope?,
        dispatcher: CoroutineContext,
        coroutineStart: CoroutineStart,
        f: suspend CoroutineScope.() -> Unit
    ): Job = (parentScope ?: scope).launch(dispatcher, coroutineStart, f)
//        try { // The user may or may not launch multiple sub suspension operations.
//            // If one of those fails, only this scope should fail instead of the parent scope.
//            coroutineScope(f)
//        } catch (e: Throwable) {
//            when (e) {
//                is CancellationException -> plugin.log.info { "Coroutine cancelled: ${e.message}" }
//                else -> plugin.log.error(e) { "Unhandled Exception from coroutine." }
//            }
//        }
}
