package dev.racci.minix.api.extensions

import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CompletableDeferred
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture

fun Plugin.registerEvents(
    vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
    vararg listeners: Listener
) = plugin.registerEvents(*listeners)

/** Sugar method for [MinixPlugin.launch] */
inline fun <R> WithPlugin<*>.sync(crossinline block: suspend () -> R) { plugin.launch { block() } }

/** Sugar method for [MinixPlugin.launchAsync] */
inline fun <R> WithPlugin<*>.async(crossinline block: suspend () -> R) { plugin.launchAsync { block() } }

/** Returns a [CompletableFuture] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.completableSync(crossinline block: suspend () -> R): CompletableFuture<R> {
    val future = CompletableFuture<R>()
    plugin.launch { future.complete(block()) }.invokeOnCompletion {
        if (it == null) return@invokeOnCompletion
        future.cancel(true)
    }
    return future
}

/** Returns a [CompletableFuture] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.completableAsync(crossinline block: suspend () -> R): CompletableFuture<R> {
    val future = CompletableFuture<R>()
    plugin.launchAsync { future.complete(block()) }.invokeOnCompletion {
        if (it == null) return@invokeOnCompletion
        future.cancel(true)
    }
    return future
}

/** Returns a [CompletableDeferred] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.deferredSync(crossinline block: suspend () -> R): CompletableDeferred<R> {
    val deferred = CompletableDeferred<R>()
    plugin.launch { deferred.complete(block()) }.invokeOnCompletion {
        if (it == null) return@invokeOnCompletion
        deferred.cancel()
    }
    return deferred
}

/** Returns a [CompletableDeferred] that is completed when suspended lambda is completed */
inline fun <R> WithPlugin<*>.deferredAsync(crossinline block: suspend () -> R): CompletableDeferred<R> {
    val deferred = CompletableDeferred<R>()
    plugin.launchAsync { deferred.complete(block()) }.invokeOnCompletion {
        if (it == null) return@invokeOnCompletion
        deferred.cancel()
    }
    return deferred
}

interface WithPlugin<T : MinixPlugin> {

    val plugin: T
}
