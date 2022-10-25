package dev.racci.minix.flowbus

import dev.racci.minix.flowbus.receiver.EventReceiver
import org.bukkit.event.Cancellable
import org.bukkit.event.EventPriority
import kotlin.reflect.KClass

/**
 * Simplified [EventReceiver.subscribeTo] for Kotlin.
 * Type of event is automatically inferred from [callback] parameter type.
 *
 * @param skipRetained Skips event already present in the flow. This is `false` by default
 * @param callback The callback function
 * @return This instance of [EventReceiver] for chaining
 */
public inline fun <reified T : Any> EventReceiver.subscribe(
    skipRetained: Boolean = false,
    noinline callback: suspend T.() -> Unit
): EventReceiver = subscribeTo(T::class, skipRetained, callback)

/**
 * Subscribes to a bukkit event, which will check for cancelling.
 * ### This doesn't current work with priority.
 *
 * @param event
 * @param priority
 * @param ignoreCancelled
 * @param callback
 * @param T
 * @receiver
 */
public inline fun <reified T : Any> EventReceiver.bukkitSubscribe(
    event: KClass<T>,
    priority: EventPriority = EventPriority.NORMAL, // TODO -> Add support for event priority
    ignoreCancelled: Boolean = false,
    crossinline callback: T.() -> Unit
) {
    this.subscribeTo(event, true) {
        if (it is Cancellable && ignoreCancelled && it.isCancelled) return@subscribeTo
        callback(it)
    }
}
