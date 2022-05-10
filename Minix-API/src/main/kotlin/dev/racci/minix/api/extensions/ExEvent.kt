@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.PluginManager
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

fun <T : Event> WithPlugin<*>.events(
    vararg events: KClass<out T>,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    listener: SimpleKListener = SimpleKListener(plugin),
    block: suspend T.() -> Unit,
) { // Lmao this shit is scuffed as fuck but it works so fuck it (I'm not even sure if it works)
    events.forEach { clazz ->
        pm.registerEvent(
            clazz.java,
            listener,
            priority,
            { _, event ->
                val dispatcher = if (forceAsync || event.isAsynchronous) {
                    plugin.asyncDispatcher
                } else plugin.minecraftDispatcher

                plugin.launch(dispatcher) {
                    if (clazz.isInstance(event)) {
                        block(event as T)
                    }
                }
            },
            plugin,
            ignoreCancelled
        )
    }
}

inline fun <reified T : Event> WithPlugin<*>.event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend T.() -> Unit
) = SimpleKListener(plugin).event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

inline fun <reified T : Event> WithPlugin<*>.subEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend (T) -> Unit
) = SimpleKListener(plugin).event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

inline fun <reified T : Event> Listener.event(
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend T.() -> Unit
) = event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

inline fun <reified T : Event> Listener.event(
    type: KClass<T>,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    crossinline block: suspend T.() -> Unit
) {
    pm.registerEvent(
        type.java,
        this@event,
        priority,
        { _, event ->
            val dispatcher = if (forceAsync || event.isAsynchronous) {
                plugin.asyncDispatcher
            } else plugin.minecraftDispatcher

            plugin.launch(dispatcher) {
                if (type.isInstance(event) && event is T) {
                    block(event)
                }
            }
        },
        plugin,
        ignoreCancelled
    )
}

inline fun WithPlugin<*>.events(block: KListener<*>.() -> Unit) = plugin.events(block)
inline fun MinixPlugin.events(block: KListener<*>.() -> Unit) = SimpleKListener(this).apply(block)

fun Listener.registerEvents(plugin: MinixPlugin) = plugin.server.pluginManager.registerEvents(this, plugin)

fun Listener.unregisterListener() = HandlerList.unregisterAll(this)

interface KListener<T : MinixPlugin> : Listener, WithPlugin<T>

val PlayerMoveEvent.displaced: Boolean
    get() = this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z

interface KotlinListener : Listener

data class SimpleKListener(override val plugin: MinixPlugin) : KListener<MinixPlugin>

fun Cancellable.cancel() {
    this.isCancelled = true
}

// TODO Create listener and auto cancel stuffs
inline fun <reified T : Event> PluginManager.callEvent(
    noinline builder: List<KParameter>.() -> Map<KParameter, Any?>
) = callEvent(T::class as KClass<Event>, builder)

fun callEvent(
    eventKClass: KClass<out Event>,
    builder: List<KParameter>.() -> Map<KParameter, Any?>
) = eventKClass.primaryConstructor!!.callBy(builder(eventKClass.primaryConstructor!!.parameters)).callEvent()
