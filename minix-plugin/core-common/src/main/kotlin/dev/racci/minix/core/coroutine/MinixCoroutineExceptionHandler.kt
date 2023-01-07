package dev.racci.minix.core.coroutine

import dev.racci.minix.api.events.plugin.CaughtCoroutineExceptionEvent
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Factory([CoroutineExceptionHandler::class])
internal class MinixCoroutineExceptionHandler(
    @InjectedParam private val plugin: MinixPlugin,
    @InjectedParam private val scope: String? = null
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        if (!plugin.enabled) {
            plugin.logger.debug { "Received exception while plugin was disabled: $exception" }
            return
        }

        if (exception is CancellationException) return

        plugin.launch {
            val event = CaughtCoroutineExceptionEvent(plugin, exception)
            getKoin().get<FlowBus>().postDeferred(event).await()
            if (event.actualCancelled) return@launch
            plugin.logger.fatal(exception) { "There was an uncaught exception in a coroutine of scope $scope." }
        }
    }
}
