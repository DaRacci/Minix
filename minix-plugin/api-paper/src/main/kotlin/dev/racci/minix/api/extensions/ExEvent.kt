package dev.racci.minix.api.extensions

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.flowbus.Priority
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.PluginManager
import org.koin.core.component.get
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

public fun <T : Event> WithPlugin<*>.events(
    vararg events: KClass<out T>,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    listener: SimpleKListener = SimpleKListener(plugin),
    block: suspend T.() -> Unit
) { // Lmao this shit is scuffed as fuck but it works so fuck it (I'm not even sure if it works)
    events.forEach { clazz ->
        pm.registerEvent(
            clazz.java,
            listener,
            priority,
            { _, event ->
                val dispatcher = if (forceAsync || event.isAsynchronous) {
                    plugin.context
                } else plugin.minecraftContext

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

/**
 * Registers an event with this extension.
 * When this extension is unloaded the event will be unregistered.
 *
 * @param T The event type.
 * @param priority The priority of the event.
 * @param ignoreCancelled Whether the event should be ignored if it is cancelled.
 * @param forceAsync Whether the event should be handled asynchronously.
 * @param block The block to execute when the event is fired.
 * @receiver The extension.
 */
public inline fun <reified T : Event> Extension<*>.event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline block: suspend T.() -> Unit
) {
    this.eventListener.event(
        type = T::class,
        plugin = plugin,
        priority = priority,
        ignoreCancelled = ignoreCancelled,
        forceAsync = false,
        block = block
    )
}

public inline fun <reified T : Event> WithPlugin<*>.event(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend T.() -> Unit
): Unit = SimpleKListener(plugin).event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

public inline fun <reified T : Event> WithPlugin<*>.subEvent(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend (T) -> Unit
): Unit = SimpleKListener(plugin).event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

public inline fun <reified T : Event> Listener.event(
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    noinline block: suspend T.() -> Unit
): Unit = event(
    type = T::class,
    plugin = plugin,
    priority = priority,
    ignoreCancelled = ignoreCancelled,
    forceAsync = forceAsync,
    block = block
)

public fun <T : Event> Listener.event(
    type: KClass<T>,
    plugin: MinixPlugin,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    forceAsync: Boolean = false,
    block: suspend T.() -> Unit
) {
    pm.registerEvent(
        type.java,
        this@event,
        priority,
        { _, event ->
            val dispatcher = if (forceAsync || event.isAsynchronous) {
                plugin.context
            } else plugin.minecraftContext

            plugin.launch(dispatcher) {
                if (type.isInstance(event) && event as? T != null) {
                    block(event)
                }
            }
        },
        plugin,
        ignoreCancelled
    )
}

public fun convertBukkitPriority(priority: EventPriority): Priority = when (priority) {
    EventPriority.HIGHEST -> Priority.FINAL
    EventPriority.HIGH -> Priority.of(35)
    EventPriority.NORMAL -> Priority.DEFAULT
    EventPriority.LOW -> Priority.of(10)
    EventPriority.LOWEST -> Priority.INITIAL
    EventPriority.MONITOR -> Priority.MONITOR
}

public inline fun WithPlugin<*>.events(block: KListener<*>.() -> Unit): SimpleKListener = plugin.events(block)
public inline fun MinixPlugin.events(block: KListener<*>.() -> Unit): SimpleKListener = SimpleKListener(this).apply(block)

public fun Listener.registerEvents(plugin: MinixPlugin): Unit = plugin.server.pluginManager.registerEvents(this, plugin)

public fun Listener.unregisterListener(): Unit = HandlerList.unregisterAll(this)

public interface KListener<T : MinixPlugin> : Listener, WithPlugin<T>

public val PlayerMoveEvent.displaced: Boolean
    get() = this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z

public interface KotlinListener : Listener

public data class SimpleKListener(override val plugin: MinixPlugin) : KListener<MinixPlugin>

public fun Cancellable.cancel() {
    this.isCancelled = true
}

// TODO Create listener and auto cancel stuffs
public inline fun <reified T : Event> PluginManager.callEvent(
    noinline builder: List<KParameter>.() -> Map<KParameter, Any?>
): Boolean = callEvent(T::class as KClass<Event>, builder)

public fun callEvent(
    eventKClass: KClass<out Event>,
    builder: List<KParameter>.() -> Map<KParameter, Any?>
): Boolean = eventKClass.primaryConstructor!!.callBy(builder(eventKClass.primaryConstructor!!.parameters)).callEvent()
