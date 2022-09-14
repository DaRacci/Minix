package dev.racci.minix.api.extensions

import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.safeCast
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asCompletableFuture
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

/** Registers all of these listeners for the plugin. */
fun Plugin.registerEvents(
    vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

interface WithPlugin<T : MinixPlugin> : KoinComponent {

    val plugin: T

    /** @see [MinixPlugin.logger] */
    val logger: MinixLogger get() = this.plugin.log

    /**
     * Registers these events for the plugin.
     *
     * @param listeners The listeners to register
     */
    fun WithPlugin<*>.registerEvents(
        vararg listeners: Listener
    ): Unit = this.plugin.registerEvents(*listeners)

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    fun <T : Any> WithPlugin<*>.getProperty(
        key: String
    ): T? = this.getKoin().getProperty("${plugin.name}:$key")

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    fun <T : Any> WithPlugin<*>.getProperty(
        key: String,
        default: T
    ): T = this.getKoin().getProperty("${plugin.name}:$key", default)

    /**
     * Sets a koin property with the plugin's prefix.
     *
     * @see [Koin.setProperty]
     */
    fun <T : Any> WithPlugin<*>.setProperty(
        key: String,
        value: T
    ): T {
        this.getKoin().setProperty("${plugin.name}:$key", value)
        return value
    }

    /**
     * Deletes a koin property with the plugin's prefix.
     *
     * @see [Koin.deleteProperty]
     */
    fun WithPlugin<*>.deleteProperty(
        key: String
    ) = this.getKoin().deleteProperty("${plugin.name}:$key")

    /** @see [MinixPlugin.launch] */
    fun launch(
        dispatcher: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val parent = this.safeCast<Extension<*>>()?.supervisor
        return this.plugin.launch(dispatcher, parent, block)
    }

    /** Launches a job on the main bukkit thread and if fired from a extension attaches as a parentJob */
    fun sync(block: suspend CoroutineScope.() -> Unit): Job {
        val parent = this.safeCast<Extension<*>>()?.supervisor
        return this.plugin.launch(plugin.minecraftDispatcher, parent, block)
    }

    /** Launches a job off the main bukkit thread and if fired from a extension attaches as a parentJob */
    fun async(block: suspend CoroutineScope.() -> Unit): Job {
        val extension = this.safeCast<Extension<*>>()
        val parent = extension?.supervisor
        val dispatcher = extension?.dispatcher?.get() ?: plugin.asyncDispatcher
        return this.plugin.launch(dispatcher, parent, block)
    }

    /**
     * A [Deferred], which is completed on the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> deferredSync(block: suspend () -> R): Deferred<R> = getDeferred(plugin.minecraftDispatcher, block)

    /**
     * A [Deferred], which is completed off the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> deferredAsync(block: suspend () -> R): Deferred<R> = getDeferred(plugin.asyncDispatcher, block)

    /**
     * A [CompletableFuture], which is completed on the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> completableSync(block: suspend () -> R): CompletableFuture<R> = getCompletable(plugin.minecraftDispatcher, block)

    /**
     * A [CompletableFuture], which is completed off the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> completableAsync(block: suspend () -> R): CompletableFuture<R> = getCompletable(plugin.asyncDispatcher, block)

    private fun <R> getDeferred(
        dispatcher: CoroutineContext,
        block: suspend () -> R
    ): Deferred<R> {
        val deferred = CompletableDeferred<R>()
        launch(dispatcher) { deferred.complete(block()) }.invokeOnCompletion { it?.let(deferred::completeExceptionally) }
        return deferred
    }

    private fun <R> getCompletable(
        dispatcher: CoroutineContext,
        block: suspend () -> R
    ): CompletableFuture<R> = getDeferred(dispatcher, block).asCompletableFuture()
}

@Deprecated("Use WithPlugin.logger instead", ReplaceWith("this.logger"))
val WithPlugin<*>.log: MinixLogger get() = this.logger

@Deprecated("Use WithPlugin.registerEvents instead", ReplaceWith("this.launch(listeners)"))
fun WithPlugin<*>.registerEvents(
    vararg listeners: Listener
) = this.registerEvents(*listeners)

@Deprecated("Use WithPlugin.sync instead", ReplaceWith("this.sync(block)"))
inline fun WithPlugin<*>.sync(noinline block: suspend CoroutineScope.() -> Unit): Job = this.sync(block)

@Deprecated("Use WithPlugin.async instead", ReplaceWith("this.async(block)"))
inline fun WithPlugin<*>.async(noinline block: suspend CoroutineScope.() -> Unit): Job = this.async(block)

@Deprecated("Use WithPlugin.completableSync instead", ReplaceWith("this.completableSync(block)"))
inline fun <R> WithPlugin<*>.completableSync(noinline block: suspend () -> R): CompletableFuture<R> = this.completableSync(block)

@Deprecated("Use WithPlugin.completableAsync instead", ReplaceWith("this.completableAsync(block)"))
inline fun <R> WithPlugin<*>.completableAsync(noinline block: suspend () -> R): CompletableFuture<R> = this.completableAsync(block)

@Deprecated("Use WithPlugin.deferredSync instead", ReplaceWith("this.deferredSync(block)"))
inline fun <R> WithPlugin<*>.deferredSync(noinline block: suspend () -> R): Deferred<R> = deferredSync(block)

@Deprecated("Use WithPlugin.deferredAsync instead", ReplaceWith("this.deferredAsync(block)"))
inline fun <R> WithPlugin<*>.deferredAsync(noinline block: suspend () -> R): Deferred<R> = deferredAsync(block)
