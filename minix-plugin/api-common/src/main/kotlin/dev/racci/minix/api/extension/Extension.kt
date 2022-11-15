package dev.racci.minix.api.extension

import dev.racci.minix.api.data.Priority
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.flowbus.EventCallback
import dev.racci.minix.flowbus.receiver.EventReceiver
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.reflect.KClass

/**
 * An Extension is a class, which is designed to basically act like it's own mini plugin.
 * With dependencies for other extensions and load states.
 *
 * @param P The owning plugin.
 * @see DataService
 */
public expect abstract class Extension<P : MinixPlugin>() : PlatformIndependentExtension<P>, EventReceiver {
    public final override fun returnOn(dispatcher: CoroutineDispatcher): EventReceiver

    public final override fun isCancelled(event: Any): Boolean

    public final override fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        priority: Priority,
        ignoreCancelled: Boolean,
        skipRetained: Boolean,
        callback: suspend (T) -> Unit
    ): EventReceiver

    public final override fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        callback: EventCallback<T>
    ): EventReceiver

    public final override fun <T : Any> unsubscribe(clazz: KClass<T>)

    public final override fun unsubscribe()
}
