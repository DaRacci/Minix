package dev.racci.minix.core.coroutine // ktlint-disable filename

import dev.racci.minix.api.events.plugin.CaughtCoroutineExceptionEvent
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Scoped
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Scoped
@Factory([CoroutineExceptionHandler::class])
@Suppress("FunctionName")
internal fun MinixCoroutineExceptionHandler(
    plugin: MinixPlugin,
    scope: String? = null
): CoroutineExceptionHandler = object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        if (!plugin.enabled) {
            plugin.logger.debug { "Received exception while plugin was disabled: $exception" }
            return
        }

        plugin.launch {
            val event = CaughtCoroutineExceptionEvent(plugin, exception)
            getKoin().get<FlowBus>().postDeferred(event).await()
            if (event.actualCancelled) return@launch
            plugin.logger.fatal(exception) { "There was an uncaught exception in a coroutine of scope $scope." }
        }
    }
}
