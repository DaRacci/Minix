@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import me.racci.raccicore.api.extensions.SimpleKListener
import me.racci.raccicore.api.extensions.WithPlugin
import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent

/**
 * Creates an event flow for [PlayerEvent] that auto filter for only events from [player].
 */
inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
) = plugin.playerEventFlow(player, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> RacciPlugin.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this),
) = playerEventFlow(player, this, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> playerEventFlow(
    player: Player,
    plugin: RacciPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin),
) = eventFlow(T::class, plugin, player, priority, ignoreCancelled, channel, listener)
    .filter { it.player.name == player.name }