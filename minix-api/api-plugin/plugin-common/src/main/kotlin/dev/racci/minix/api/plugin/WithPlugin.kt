package dev.racci.minix.api.plugin

import dev.racci.minix.api.logger.MinixLogger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.asCompletableFuture
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

public expect interface WithPlugin<T : MinixPlugin> : KoinComponent {

    public val plugin: T

    public val logger: MinixLogger

    /**
     * Registers these events for the plugin.
     *
     * @param listeners The listeners to register
     */
    fun registerEvents(
        vararg listeners: Listener
    ): Unit = this.plugin.registerEvents(*listeners)

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    fun <T : Any> getProperty(
        key: String
    ): T? = this.getKoin().getProperty("${this.plugin.name}:$key")

    /**
     * Retrieves a koin property with the plugin's prefix.
     *
     * @see [Koin.getProperty]
     */
    fun <T : Any> getProperty(
        key: String,
        default: T
    ): T = this.getKoin().getProperty("${this.plugin.name}:$key", default)

    /**
     * Sets a koin property with the plugin's prefix.
     *
     * @see [Koin.setProperty]
     */
    fun <T : Any> setProperty(
        key: String,
        value: T
    ): T {
        this.getKoin().setProperty("${this.plugin.name}:$key", value)
        return value
    }

    /**
     * Deletes a koin property with the plugin's prefix.
     *
     * @see [Koin.deleteProperty]
     */
    fun deleteProperty(
        key: String
    ) = this.getKoin().deleteProperty("${this.plugin.name}:$key")

    /** @see [MinixPlugin.launch] */
    fun launch(
        dispatcher: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(dispatcher, block)

    /** Launches a job on the main bukkit thread and if fired from a extension attaches as a parentJob */
    fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(this.plugin.minecraftDispatcher, block)

    /** Launches a job off the main bukkit thread and if fired from a extension attaches as a parentJob */
    fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.plugin.launch(this.plugin.asyncDispatcher, block)

    /**
     * A [Deferred], which is completed on the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> deferredSync(block: suspend () -> R): Deferred<R> = getDeferred(this.plugin.minecraftDispatcher, block)

    /**
     * A [Deferred], which is completed off the server thread.
     * This deferred will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> deferredAsync(block: suspend () -> R): Deferred<R> = getDeferred(this.plugin.asyncDispatcher, block)

    /**
     * A [CompletableFuture], which is completed on the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> completableSync(block: suspend () -> R): CompletableFuture<R>
    /**
     * A [CompletableFuture], which is completed off the server thread.
     * This future will be completed after the [block] is executed.
     *
     * @param block The lambda to run.
     * @param R The return type of the lambda.
     */
    fun <R> completableAsync(block: suspend () -> R): CompletableFuture<R>
    private fun <R> getDeferred(
        dispatcher: CoroutineContext,
        block: suspend () -> R
    ): Deferred<R>

    private fun <R> getCompletable(
        dispatcher: CoroutineContext,
        block: suspend () -> R
    ): CompletableFuture<R>
}
