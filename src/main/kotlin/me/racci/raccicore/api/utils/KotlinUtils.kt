@file:Suppress("UNUSED")
package me.racci.raccicore.api.utils

import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

inline fun <reified T : Throwable, reified U : Any> catch(
    err: (T) -> U,
    run: () -> U
): U = try {
    run()
} catch (ex: Throwable) {
    if (ex is T) err(ex) else throw ex
}

inline fun <reified T : Throwable> catch(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> Unit
): Unit = catch<T, Unit>(err, run)

inline fun <reified T : Throwable, reified U : Any> catch(
    default: U,
    run: () -> U
): U = catch<T, U>({ default }, run)

inline fun <reified T : Throwable, R> catchAndReturn(
    err: (T) -> Unit = {it.printStackTrace()},
    run: () -> R
): R? = try {
    run()
} catch (ex: Exception) {
    if(ex is T) err(ex) else throw ex
    null
}

inline fun <reified T : Event> RacciPlugin.listen(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (T) -> Unit
) = pm.registerEvent(T::class.java, object : KotlinListener {},
    priority, { _, it -> if(it is T) callback(it) },
    this, ignoreCancelled
)

fun <T> T.not(other: T) = takeUnless { it == other }
fun <T> T.notIn(container: Iterable<T>) = takeUnless { it in container }