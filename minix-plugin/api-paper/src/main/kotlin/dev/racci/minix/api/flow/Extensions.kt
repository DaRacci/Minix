package dev.racci.minix.api.flow // ktlint-disable filename

import dev.racci.minix.api.extensions.SimpleKListener
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import kotlin.reflect.KClass

/**
 * Creates an event flow for [PlayerEvent] that auto filter for only events from [player].
 */
public inline fun <reified T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
): Flow<T> = plugin.playerEventFlow(T::class, player, priority, ignoreCancelled, channel, listener)

public inline fun <T : PlayerEvent> WithPlugin<*>.playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
): Flow<T> = plugin.playerEventFlow(kClass, player, priority, ignoreCancelled, channel, listener)

public inline fun <reified T : PlayerEvent> MinixPlugin.playerEventFlow(
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this)
): Flow<T> = playerEventFlow(T::class, player, this, priority, ignoreCancelled, channel, listener)

public inline fun <T : PlayerEvent> MinixPlugin.playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(this)
): Flow<T> = playerEventFlow(kClass, player, this, priority, ignoreCancelled, channel, listener)

public inline fun <reified T : PlayerEvent> playerEventFlow(
    player: Player,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
): Flow<T> = playerEventFlow(T::class, player, plugin, priority, ignoreCancelled, channel, listener)

public inline fun <T : PlayerEvent> playerEventFlow(
    kClass: KClass<T>,
    player: Player,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    channel: Channel<T> = Channel(Channel.CONFLATED),
    listener: Listener = SimpleKListener(plugin)
): Flow<T> = eventFlow(kClass, plugin, player, priority, ignoreCancelled, false, channel, listener)
    .filter { it.player.name == player.name }
