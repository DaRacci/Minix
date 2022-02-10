@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.registerEvents
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.LinkedList

typealias WhenPlayerQuitCollectionCallback = Player.() -> Unit
typealias WhenPlayerQuitMapCallback<V> = Player.(V) -> Unit

// List

fun MinixPlugin.onlinePlayerListOf() = OnlinePlayerList(this)

fun WithPlugin<*>.onlinePlayerListOf() = plugin.onlinePlayerListOf()

fun onlinePlayerListOf(
    vararg players: Player,
    plugin: MinixPlugin,
) = OnlinePlayerList(plugin).apply { addAll(players) }

fun MinixPlugin.onlinePlayerListOf(vararg players: Player) = onlinePlayerListOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg players: Player) = plugin.onlinePlayerListOf(*players)

fun onlinePlayerListOf(
    vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>,
    plugin: MinixPlugin,
) = OnlinePlayerList(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun MinixPlugin.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>) =
    onlinePlayerListOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>) =
    plugin.onlinePlayerListOf(*pair)

// Set

fun MinixPlugin.onlinePlayerSetOf() = OnlinePlayerSet(this)

fun WithPlugin<*>.onlinePlayerSetOf() = plugin.onlinePlayerSetOf()

fun onlinePlayerSetOf(
    vararg players: Player,
    plugin: MinixPlugin,
) = OnlinePlayerSet(plugin).apply { addAll(players) }

fun MinixPlugin.onlinePlayerSetOf(vararg players: Player) = onlinePlayerSetOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg players: Player) = plugin.onlinePlayerSetOf(*players)

fun onlinePlayerSetOf(
    vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>,
    plugin: MinixPlugin,
) = OnlinePlayerSet(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun MinixPlugin.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>) =
    onlinePlayerSetOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>) =
    plugin.onlinePlayerSetOf(*pair)

// Map

fun <V> MinixPlugin.onlinePlayerMapOf() = OnlinePlayerMap<V>(this)

fun <V> WithPlugin<*>.onlinePlayerMapOf() = plugin.onlinePlayerMapOf<V>()

fun <V> onlinePlayerMapOf(
    vararg pair: Pair<Player, V>,
    plugin: MinixPlugin,
) = OnlinePlayerMap<V>(plugin).apply { putAll(pair) }

fun <V> MinixPlugin.onlinePlayerMapOf(vararg pair: Pair<Player, V>) = onlinePlayerMapOf(*pair, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg pair: Pair<Player, V>) = plugin.onlinePlayerMapOf(*pair)

fun <V> onlinePlayerMapOf(
    vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>,
    plugin: MinixPlugin,
) = OnlinePlayerMap<V>(plugin).apply {
    triple.forEach { (player, value, whenPlayerQuit) ->
        put(
            player,
            value,
            whenPlayerQuit
        )
    }
}

fun <V> MinixPlugin.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>) =
    onlinePlayerMapOf(*triple, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>) =
    plugin.onlinePlayerMapOf(*triple)

class OnlinePlayerList(override val plugin: MinixPlugin) : LinkedList<Player>(), OnlinePlayerCollection {

    private val whenQuit: MutableList<Pair<Player, WhenPlayerQuitCollectionCallback>> = LinkedList()

    override fun add(
        player: Player,
        whenPlayerQuit: Player.() -> Unit,
    ): Boolean = if (super<OnlinePlayerCollection>.add(player, whenPlayerQuit)) {
        whenQuit.add(player to whenPlayerQuit)
        true
    } else false

    override fun add(element: Player): Boolean {
        if (isEmpty()) registerEvents(plugin)
        return super<LinkedList>.add(element)
    }

    override fun quit(player: Player): Boolean = if (super.quit(player)) {
        val iterator = whenQuit.iterator()
        for (pair in iterator) {
            if (pair.first == player) {
                iterator.remove()
                pair.second.invoke(pair.first)
            }
        }
        true
    } else false

    override fun removeFirst(): Player = super.removeFirst().also {
        checkRegistration()
    }

    override fun removeLastOccurrence(p0: Any?): Boolean = super.removeLastOccurrence(p0).also {
        if (it) checkRegistration()
    }

    override fun removeAt(p0: Int): Player = super.removeAt(p0).also {
        checkRegistration()
    }

    override fun remove(element: Player): Boolean = if (super.remove(element)) {
        checkRegistration()
        true
    } else false

    override fun removeLast(): Player = super.removeLast().also {
        checkRegistration()
    }
}

class OnlinePlayerSet(override val plugin: MinixPlugin) : HashSet<Player>(), OnlinePlayerCollection {

    private val whenQuit: MutableMap<Player, WhenPlayerQuitCollectionCallback> = mutableMapOf()

    override fun add(
        player: Player,
        whenPlayerQuit: WhenPlayerQuitCollectionCallback,
    ): Boolean = if (super<OnlinePlayerCollection>.add(player, whenPlayerQuit)) {
        whenQuit[player] = whenPlayerQuit

        checkRegistration()
        true
    } else false

    override fun add(element: Player): Boolean = super<HashSet>.add(element).also {
        if (it) checkRegistration()
    }

    override fun remove(element: Player): Boolean = super.remove(element).also {
        if (it) checkRegistration()
    }

    override fun quit(player: Player): Boolean = if (super.quit(player)) {
        whenQuit.remove(player)?.also { block ->
            block.invoke(player)
        }
        true
    } else false
}

interface OnlinePlayerCollection : MutableCollection<Player>, KListener<MinixPlugin> {

    fun checkRegistration() {
        if (size == 1) {
            event<PlayerQuitEvent> { quit(player) }
            event<PlayerKickEvent> { quit(player) }
        } else if (size == 0) {
            unregisterListener()
        }
    }

    /**
     * Adds a new Player to the collection with a callback for when the player quits the server.
     */
    fun add(
        player: Player,
        whenPlayerQuit: WhenPlayerQuitCollectionCallback,
    ): Boolean = add(player).also {
        if (it) checkRegistration()
    }

    /**
     * Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback] provided.
     */
    fun quit(player: Player): Boolean = remove(player).also {
        if (it) checkRegistration()
    }

    /**
     * Clear the collection calling all [WhenPlayerQuitCollectionCallback] from the Players.
     */
    fun clearQuiting() {
        toMutableList().forEach {
            quit(it)
        }
    }
}

class OnlinePlayerMap<V>(override val plugin: MinixPlugin) : HashMap<Player, V>(), KListener<MinixPlugin> {

    private val whenQuit: HashMap<Player, WhenPlayerQuitMapCallback<V>> = hashMapOf()

    /**
     * Puts a Player to the map with a [value] and a callback for when the player quits the server.
     */
    fun put(
        key: Player,
        value: V,
        whenPlayerQuit: WhenPlayerQuitMapCallback<V>,
    ): V? {
        whenQuit[key] = whenPlayerQuit
        return put(key, value).also {
            checkRegistration()
        }
    }

    /**
     * Removes the player from the map, calling the [WhenPlayerQuitMapCallback] provided.
     */
    fun quit(player: Player) {
        remove(player)?.also {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player, it)
            }
            checkRegistration()
        }
    }

    /**
     * Clear the map calling all [WhenPlayerQuitMapCallback] from the Players.
     */
    fun clearQuiting() {
        keys.toMutableList().forEach {
            quit(it)
        }
    }

    override fun remove(key: Player): V? = super.remove(key).also {
        checkRegistration()
    }

    override fun remove(
        key: Player,
        value: V,
    ): Boolean = super.remove(key, value).also {
        checkRegistration()
    }

    private fun checkRegistration() {
        if (size == 1) {
            event<PlayerQuitEvent> { quit(player) }
            event<PlayerKickEvent> { quit(player) }
        } else if (size == 0) {
            unregisterListener()
        }
    }
}
