package dev.racci.minix.api.events

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import kotlin.reflect.KClass

/**
 * Represents an event.
 *
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 * @see PlayerUnloadEvent
 *
 * @param async If the event is Asynchronous.
 */
abstract class KEvent(
    async: Boolean = false
) : Event(async), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]

    companion object {
        val handlerMap: LoadingCache<KClass<out Event>, HandlerList> = Caffeine.newBuilder().build { HandlerList() }
    }
}
