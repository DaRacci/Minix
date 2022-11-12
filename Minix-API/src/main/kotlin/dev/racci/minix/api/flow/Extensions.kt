@file:Suppress("UNUSED")

package dev.racci.minix.api.flow

import dev.racci.minix.api.extensions.SimpleKListener
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import kotlin.reflect.KClass

/**
 * Creates an event flow for [PlayerEvent] that auto filter for only events from [player].
 */
inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
) = plugin.playerEventFlow(T::class, player, priority, ignoreCancelled, channel, listener)

inline fun <T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
) = plugin.playerEventFlow(kClass, player, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> MinixPlugin.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this)
) = playerEventFlow(T::class, player, this, priority, ignoreCancelled, channel, listener)

inline fun <T : PlayerEvent> MinixPlugin.playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this)
) = playerEventFlow(kClass, player, this, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> playerEventFlow(
    player: Player,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
) = playerEventFlow(T::class, player, plugin, priority, ignoreCancelled, channel, listener)

inline fun <T : PlayerEvent> playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
) = eventFlow(kClass, plugin, player, priority, ignoreCancelled, false, channel, listener)
    .filter { it.player.name == player.name }