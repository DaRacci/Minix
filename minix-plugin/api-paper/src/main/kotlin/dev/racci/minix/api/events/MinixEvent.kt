package dev.racci.minix.api.events

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import kotlin.reflect.KClass

/**
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 *
 * @param async If the event is Asynchronous.
 */
public actual abstract class MinixEvent(
    public actual val async: Boolean = true
) : Event(async) {

    final override fun getHandlers(): HandlerList = handlerMap[this::class]

    internal companion object {
        val handlerMap: LoadingCache<KClass<out Event>, HandlerList> = Caffeine.newBuilder().build { HandlerList() }
    }
}
