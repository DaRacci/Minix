package dev.racci.minix.api.plugin

import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.logger.MinixLogger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asCompletableFuture
import org.bukkit.event.Listener
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

public interface WithPlugin<T : MinixPlugin> : KoinComponent {

    public val plugin: T

    public val logger: MinixLogger get() = plugin.logger

    public val dataFolder: Path get() = plugin.dataFolder

    /**
     * Registers these events for the plugin.
     *
     * @param listeners The listeners to register
     */
    public fun registerEvents(
        vararg listeners: Listener
    ): Unit = this.plugin.registerEvents(*listeners)

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    public fun <T : Any> getProperty(
        key: String
    ): T? = this.getKoin().getProperty("${this.plugin.value}:$key")

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    public fun <T : Any> getProperty(
        key: String,
        default: T
    ): T = this.getKoin().getProperty("${this.plugin.value}:$key", default)

    /**
     * Sets a koin property with the plugin's prefix.
     *
     * @see [Koin.setProperty]
     */
    public fun <T : Any> setProperty(
        key: String,
        value: T
    ): T {
        this.getKoin().setProperty("${this.plugin.value}:$key", value)
        return value
    }

    /**
     * Deletes a koin property with the plugin's prefix.
     *
     * @see [Koin.deleteProperty]
     */
    public fun deleteProperty(
        key: String
    ): Unit = this.getKoin().deleteProperty("${this.plugin.value}:$key")

    /** @see [MinixPlugin.launch] */
    public fun launch(
        dispatcher: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(dispatcher, block)

    /** Launches a job on the main bukkit thread and if fired from a extension attaches as a parentJob */
    public fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(this.plugin.minecraftDispatcher, block)

    /** Launches a job off the main bukkit thread and if fired from a extension attaches as a parentJob */
    public fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(this.plugin.asyncDispatcher, block)

    /**
     * A [Deferred], which is completed on the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public suspend fun <R> deferredSync(block: suspend () -> R): Deferred<R> = getDeferred(this.plugin.minecraftDispatcher, block)

    /**
     * A [Deferred], which is completed off the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public fun <R> deferredAsync(block: suspend () -> R): Deferred<R> = getDeferred(this.plugin.asyncDispatcher, block)

    /**
     * A [CompletableFuture], which is completed on the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public fun <R> completableSync(block: suspend () -> R): CompletableFuture<R> = getCompletable(this.plugin.minecraftDispatcher, block)

    /**
     * A [CompletableFuture], which is completed off the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    public fun <R> completableAsync(block: suspend () -> R): CompletableFuture<R> = getCompletable(this.plugin.asyncDispatcher, block)

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
