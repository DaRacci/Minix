@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.reflect.KClass

inline fun <reified T : Event> KListener<*>.event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline block: T.() -> Unit
) = event(plugin, priority, ignoreCancelled, block)

fun <T : Event> KListener<*>.event(
    type: KClass<T>,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    block: T.() -> Unit
) = event(plugin, type, priority, ignoreCancelled, block)

inline fun <reified T : Event> Listener.event(
    plugin: RacciPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline block: T.() -> Unit
) {
    event(plugin, T::class, priority, ignoreCancelled, block)
}

fun <T : Event> Listener.event(
    plugin: RacciPlugin,
    type: KClass<T>,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    block: T.() -> Unit
) {
    Bukkit.getServer().pluginManager.registerEvent(
        type.java,
        this,
        priority,
        { _, event ->
            if(type.isInstance(event)) {

                (event as? T)?.block()
            }
        },
        plugin,
        ignoreCancelled
    )
}

inline fun WithPlugin<*>.events(block: KListener<*>.() -> Unit) = plugin.events(block)
inline fun RacciPlugin.events(block: KListener<*>.() -> Unit) = SimpleKListener(this).apply(block)

fun Listener.registerEvents(plugin: RacciPlugin)
        = plugin.server.pluginManager.registerEvents(this, plugin)

fun Listener.unregisterListener() = HandlerList.unregisterAll(this)

interface KListener<T : RacciPlugin> : Listener, WithPlugin<T>

val PlayerMoveEvent.displaced: Boolean
    get() = this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z

interface KotlinListener : Listener

class SimpleKListener(override val plugin: RacciPlugin) : KListener<RacciPlugin>

fun Cancellable.cancel() {this.isCancelled = true}