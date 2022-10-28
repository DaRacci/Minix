package dev.racci.minix.api.plugin

import dev.racci.minix.api.logger.MinixLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

public expect interface WithPlugin<in T : MinixPlugin> : KoinComponent {

    /** A reference to the plugin instance. */
    public val plugin: @UnsafeVariance T

    /** This plugin's main logger. */
    public open val logger: MinixLogger

    /** This plugin's data folder. */
    public open val dataFolder: Path

    /** The plugins asynchronous context. */
    public open val context: CoroutineContext

    /** The plugins parent scope and error handler. */
    public open val coroutineScope: CoroutineScope

    /**
     * Launches the given function in the Coroutine Scope of the given plugin.
     * This function may be called immediately without any delay if already on
     * a thread that is able to dispatch this request.
     * @param context Coroutine context. The default context is async context.
     * @param block callback function inside a coroutine scope.
     * @return Cancelable coroutine job.
     */
    public open fun launch(
        context: CoroutineContext = this.context,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    /** Syntax sugar for [launch] with the default context. */
    public open fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job

    /**
     * A [Deferred], which is completed off the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public open fun <R> deferredAsync(
        block: suspend () -> R
    ): Deferred<R>

    /**
     * A [CompletableFuture], which is completed off the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public open fun <R> completableAsync(
        block: suspend () -> R
    ): CompletableFuture<R>
}
