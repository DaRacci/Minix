@file:OptIn(MinixInternal::class)

package dev.racci.minix.api.extensions

import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.safeCast
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

/** Registers all of these listeners for the plugin. */
fun Plugin.registerEvents(
    vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

/** Registers all of these listeners for the plugin of the [WithPlugin] class */
fun WithPlugin<*>.registerEvents(
    vararg listeners: Listener
) = plugin.registerEvents(*listeners)

/** Launches a job on the main bukkit thread and if fired from a extension attaches as a parentJob */
inline fun WithPlugin<*>.sync(noinline block: suspend CoroutineScope.() -> Unit): Job {
    val parent = this.safeCast<Extension<*>>()?.supervisor
    return plugin.launch(plugin.minecraftDispatcher, parent, block)
}

/** Launches a job off the main bukkit thread and if fired from a extension attaches as a parentJob */
inline fun WithPlugin<*>.async(noinline block: suspend CoroutineScope.() -> Unit): Job {
    val parent = this.safeCast<Extension<*>>()?.supervisor
    return plugin.launch(plugin.asyncDispatcher, parent, block)
}

/** Returns a [CompletableFuture] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.completableSync(crossinline block: suspend () -> R): CompletableFuture<R> = completable(plugin.minecraftDispatcher, block)

/** Returns a [CompletableFuture] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.completableAsync(crossinline block: suspend () -> R): CompletableFuture<R> = completable(plugin.asyncDispatcher, block)

/** Returns a [Deferred] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.deferredSync(crossinline block: suspend () -> R): Deferred<R> = deferred(plugin.minecraftDispatcher, block)

/** Returns a [Deferred] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.deferredAsync(crossinline block: suspend () -> R): Deferred<R> = deferred(plugin.asyncDispatcher, block)

inline fun <R> WithPlugin<*>.deferred(
    dispatcher: CoroutineContext,
    crossinline block: suspend () -> R
): Deferred<R> {
    val deferred = CompletableDeferred<R>()
    launch(dispatcher) { deferred.complete(block()) }.invokeOnCompletion { it?.let(deferred::completeExceptionally) }
    return deferred
}

inline fun <R> WithPlugin<*>.completable(
    dispatcher: CoroutineContext,
    crossinline block: suspend () -> R
): CompletableFuture<R> {
    val future = CompletableFuture<R>()
    launch(dispatcher) { future.complete(block()) }.invokeOnCompletion(future::completeExceptionally)
    return future
}

fun WithPlugin<*>.launch(
    dispatcher: CoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val parent = this.safeCast<Extension<*>>()?.supervisor
    return plugin.launch(dispatcher, parent, block)
}

interface WithPlugin<T : MinixPlugin> {

    val plugin: T
}
