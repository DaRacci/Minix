package dev.racci.minix.api.events

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.events.KEvent.Companion.handlerMap
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.world.WorldEvent
import kotlin.reflect.KClass

/**
 * Represents an event.
 *
 * ## Includes Pre done handlers and cancellable params,
 * however you still need to add your own static handler
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

/**
 * Represents a player event.
 *
 * ## Includes Pre done handlers and cancellable params,
 * however you still need to add your own static handler
 * @see PlayerMoveXYZEvent
 *
 * @param player The player of the event.
 * @param async If the event is Asynchronous.
 */
abstract class KPlayerEvent(
    player: Player,
    async: Boolean = false
) : PlayerEvent(player, async), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]
}

/**
 * Represents a World event,
 * Includes variables [isOverworld] [isNether] and [isEnd]
 * These make it easy to find what world type the event is within.
 *
 * ## Includes Pre done handlers and cancellable params
 *
 * @param world The world which this event happened in.
 */
abstract class KWorldEvent(
    world: World
) : WorldEvent(world), Cancellable {

    val isOverworld: Boolean
        get() = world.environment == World.Environment.NORMAL
    val isNether: Boolean
        get() = world.environment == World.Environment.NETHER
    val isEnd: Boolean
        get() = world.environment == World.Environment.THE_END
    val isCustom: Boolean
        get() = world.environment == World.Environment.CUSTOM

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]
}
