package dev.racci.minix.flowbus.receiver

import dev.racci.minix.flowbus.EventCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.reflect.KClass

public interface EventReceiver {
    /**
     * Set the `CoroutineDispatcher` which will be used to launch your callbacks.
     *
     * If this [EventReceiver] was created on the main thread the default dispatcher will be [Dispatchers.Main].
     * In any other case [Dispatchers.Default] will be used.
     */
    public fun returnOn(dispatcher: CoroutineDispatcher): EventReceiver

    /**
     * Subscribe to events that are type of [clazz] with the given [callback] function.
     * The [callback] can be called immediately if event of type [clazz] is present in the flow.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback The callback function
     * @return This instance of [EventReceiver] for chaining
     */
    public fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        skipRetained: Boolean = false,
        callback: suspend (T) -> Unit
    ): EventReceiver

    /**
     * A variant of [subscribeTo] that uses an instance of [EventCallback] as callback.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback Interface with implemented callback function
     * @return This instance of [EventReceiver] for chaining
     * @see [subscribeTo]
     */
    public fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        skipRetained: Boolean = false,
        callback: EventCallback<T>
    ): EventReceiver

    /**
     * Unsubscribe from events type of [clazz]
     */
    public fun <T : Any> unsubscribe(clazz: KClass<T>)

    /**
     * Unsubscribe from all events
     */
    public fun unsubscribe()
}
