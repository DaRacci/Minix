package dev.racci.minix.api.extension

import dev.racci.minix.api.data.Priority
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.flowbus.EventCallback
import dev.racci.minix.flowbus.receiver.EventReceiver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * An Extension is a class, which is designed to basically act like it's own mini plugin.
 * With dependencies for other extensions and load states.
 *
 * @param P The owning plugin.
 * @see DataService
 */
public expect abstract class Extension<P : MinixPlugin>() : PlatformIndependentExtension<P>, EventReceiver {
    final override val EventReceiver.exceptionHandler: CoroutineExceptionHandler

    final override val EventReceiver.supervisorScope: CoroutineScope

    final override fun returnOn(dispatcher: CoroutineDispatcher): EventReceiver

    final override fun isCancelled(event: Any): Boolean

    final override fun EventReceiver.createEventScope(): CoroutineScope

    final override fun <T : Any> EventReceiver.subscribeTo(
        clazz: KClass<T>,
        priority: Priority,
        ignoreCancelled: Boolean,
        skipRetained: Boolean,
        callback: suspend (T) -> Unit
    ): EventReceiver

    final override fun <T : Any> EventReceiver.subscribeTo(
        clazz: KClass<T>,
        callback: EventCallback<T>
    ): EventReceiver

    public final override fun <T : Any> EventReceiver.flowOf(
        clazz: KClass<T>,
        priority: Priority,
        ignoreCancelled: Boolean,
        skipRetained: Boolean
    ): Flow<T>

    final override fun <T : Any> EventReceiver.unsubscribe(clazz: KClass<T>)

    final override fun EventReceiver.unsubscribe()
}
