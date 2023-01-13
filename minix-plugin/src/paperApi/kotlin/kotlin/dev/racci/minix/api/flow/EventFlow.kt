@file:Suppress("UNUSED")

package dev.racci.minix.api.flow

import dev.racci.minix.api.extensions.SimpleKListener
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.events
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.reflect.KClass

public inline fun <reified T : Event> WithPlugin<*>.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = plugin.events {},
    assignListener: Listener = plugin.events {}
): Flow<T> = plugin.eventFlow(assign, priority, ignoreCancelled, forceAsync, channel, listener, assignListener)

public inline fun <T : Event> WithPlugin<*>.eventFlow(
    event: KClass<T>,
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = plugin.events {},
    assignListener: Listener = plugin.events {}
): Flow<T> = eventFlow(event, plugin, assign, priority, ignoreCancelled, forceAsync, channel, listener, assignListener)

public inline fun <reified T : Event> MinixPlugin.eventFlow(
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = events {},
    assignListener: Listener = events {}
): Flow<T> = eventFlow(T::class, this, assign, priority, ignoreCancelled, forceAsync, channel, listener, assignListener)

/**
 * Create an Event Flow that receives the specified Event [type].
 *
 * [assign] is use for auto cancel the Flow when the Player disconnects.
 *
 * @param T The event type.
 * @param type The class of the event type.
 * @param plugin The calling plugin.
 * @param assign The player in which the flow is assigned to.
 * @param priority The [EventPriority] level
 * @param ignoreCancelled Weather to ignore cancelled events or not.
 * @param forceAsync If this event should be called async event if the event itself isn't.
 * @param channel ?
 * @param listener ?
 * @param assignListener ?
 * @return
 */
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : Event> eventFlow(
    type: KClass<T>,
    plugin: MinixPlugin,
    assign: Player? = null,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
    assignListener: Listener = SimpleKListener(plugin)
): Flow<T> {
    val flow = channel.consumeAsFlow().onStart {
        listener.event(type, plugin, priority, ignoreCancelled, forceAsync) {
            if (!channel.isClosedForSend && !channel.isClosedForReceive) {
                channel.send(this@event)
            }
        }
    }

    val assignedListener: Listener? = if (assign != null) {
        assignListener.apply {
            val closeChannel = { player: Player ->
                if (!channel.isClosedForSend && player.name == assign.name) {
                    channel.close()
                }
            }

            event<PlayerQuitEvent>(plugin) { closeChannel(player) }
            event<PlayerKickEvent>(plugin) { closeChannel(player) }
        }
    } else null

    channel.invokeOnClose {
        listener.unregisterListener()
        assignedListener?.unregisterListener()
    }

    return flow
}
