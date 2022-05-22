package dev.racci.minix.api.extension

import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.flow.filter
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

/**
 * Extension states, which describe what state of loading/unloading an extension is currently in.
 **/
enum class ExtensionState {

    FAILED_LOADING,
    FAILED_ENABLING,
    FAILED_UNLOADING,
    FAILED_DEPENDENCIES,

    LOADED,
    LOADING,

    ENABLED,
    ENABLING,

    UNLOADED,
    UNLOADING,
}

/** Registers a listener for events from this extension. */
suspend inline fun <reified T : Extension<P>, P : MinixPlugin> extensionEvent(
    plugin: P,
    event: ExtensionState,
    extension: KClass<out T> = T::class,
    crossinline block: suspend T.() -> Unit
) {
    PluginService[plugin].extensionEvents
        .filter { it.extension.instanceOf(extension) && it.state == event }
        .collect { block.invoke(it.extension as T) }
}

/** Registers a listener for events from this extension. */
suspend inline fun <reified T : Extension<P>, P : MinixPlugin> WithPlugin<P>.extensionEvent(
    event: ExtensionState,
    extension: KClass<out T> = T::class,
    crossinline block: suspend T.() -> Unit
) = extensionEvent(plugin, event, extension, block)

@ApiStatus.Internal
@ApiStatus.NonExtendable
suspend inline fun send(plugin: MinixPlugin, event: ExtensionStateEvent) {
    PluginService[plugin].extensionEvents.emit(event)
}
