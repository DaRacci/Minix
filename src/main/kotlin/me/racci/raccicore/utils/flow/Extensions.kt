package me.racci.raccicore.utils.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import me.racci.raccicore.utils.extensions.SimpleKListener
import me.racci.raccicore.utils.extensions.WithPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.Plugin

/**
 * Creates a event flow for [PlayerEvent] that auto filter for only events from [player].
 */
inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
        player: Player,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel(Channel.CONFLATED),
        listener: Listener = SimpleKListener(plugin),
) = plugin.playerEventFlow(player, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> Plugin.playerEventFlow(
        player: Player,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel(Channel.CONFLATED),
        listener: Listener = SimpleKListener(this),
) = playerEventFlow(player, this, priority, ignoreCancelled, channel, listener)

inline fun <reified T : PlayerEvent> playerEventFlow(
        player: Player,
        plugin: Plugin,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        channel: Channel<T> = Channel(Channel.CONFLATED),
        listener: Listener = SimpleKListener(plugin),
) = eventFlow(T::class, plugin, player, priority, ignoreCancelled, channel, listener)
        .filter { it.player.name == player.name }