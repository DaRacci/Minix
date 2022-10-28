package dev.racci.minix.api.plugin

import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.services.PluginService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asCompletableFuture
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

// TODO -> Implement basic event receiver and flow additions
public actual interface WithPlugin<in T : MinixPlugin> : KoinComponent {

    /** A reference to the plugin instance. */
    public actual val plugin: @UnsafeVariance T

    /** This plugin's main logger. */
    public actual val logger: MinixLogger get() = plugin.logger

    /** This plugin's data folder. */
    public actual val dataFolder: Path get() = plugin.dataFolder

    /** The plugins asynchronous context. */
    public actual val context: CoroutineContext get() = PluginService.coroutineSession[plugin].context

    /** The plugins parent scope and error handler. */
    public actual val coroutineScope: CoroutineScope get() = PluginService.coroutineSession[plugin].coroutineScope

    /** A bukkit main thread confined coroutine context. */
    public val minecraftContext: CoroutineContext get() = PluginService.coroutineSession[plugin].minecraftContext

    /**
     * Launches the given function in the Coroutine Scope of the given plugin.
     * This function may be called immediately without any delay if already on
     * a thread that is able to dispatch this request.
     * @param context Coroutine context. The default context is async context.
     * @param block callback function inside a coroutine scope.
     * @return Cancelable coroutine job.
     */
    public actual fun launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[plugin].launch(context, block = block)

    /**
     * Launches the given function in the Coroutine Scope of the given plugin.
     * This function may be called immediately without any delay if already on the bukkit main thread.
     * This means, for example, that event cancelling or modifying return values is still possible.
     * @param block callback function inside a coroutine scope.
     * @return Cancelable coroutine job.
     */
    public fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = launch(this.minecraftContext, block = block)

    /** Syntax sugar for [launch] with the default context. */
    public actual fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.launch(this.context, block)

    /**
     * A [Deferred], which is completed on the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public suspend fun <R> deferredSync(
        block: suspend () -> R
    ): Deferred<R> = getDeferred(this.plugin.minecraftContext, block)

    /**
     * A [Deferred], which is completed off the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public actual fun <R> deferredAsync(
        block: suspend () -> R
    ): Deferred<R> = getDeferred(this.plugin.context, block)

    /**
     * A [CompletableFuture], which is completed on the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public fun <R> completableSync(
        block: suspend () -> R
    ): CompletableFuture<R> = getCompletable(this.plugin.minecraftContext, block)

    /**
     * A [CompletableFuture], which is completed off the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public actual fun <R> completableAsync(
        block: suspend () -> R
    ): CompletableFuture<R> = getCompletable(this.plugin.context, block)

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

    /**
     * Registers these events for the plugin.
     *
     * @param listeners The listeners to register
     */
    public fun registerEvents(
        vararg listeners: Listener
    ): Unit = this.plugin.registerEvents(*listeners)
}
