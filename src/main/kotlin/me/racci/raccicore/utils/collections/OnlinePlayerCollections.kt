package me.racci.raccicore.utils.collections

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.extensions.*
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

typealias WhenPlayerQuitCollectionCallback = Player.() -> Unit
typealias WhenPlayerQuitMapCallback<V> = Player.(V) -> Unit

// List

fun RacciPlugin.onlinePlayerListOf() = OnlinePlayerList(this)

fun WithPlugin<*>.onlinePlayerListOf() = plugin.onlinePlayerListOf()

fun onlinePlayerListOf(vararg players: Player, plugin: RacciPlugin)
        = OnlinePlayerList(plugin).apply { addAll(players) }

fun RacciPlugin.onlinePlayerListOf(vararg players: Player)
        = onlinePlayerListOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg players: Player)
        = plugin.onlinePlayerListOf(*players)

fun onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: RacciPlugin)
        = OnlinePlayerList(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun RacciPlugin.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = onlinePlayerListOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = plugin.onlinePlayerListOf(*pair)

// Set

fun RacciPlugin.onlinePlayerSetOf() = OnlinePlayerSet(this)

fun WithPlugin<*>.onlinePlayerSetOf() = plugin.onlinePlayerSetOf()

fun onlinePlayerSetOf(vararg players: Player, plugin: RacciPlugin)
        = OnlinePlayerSet(plugin).apply { addAll(players) }

fun RacciPlugin.onlinePlayerSetOf(vararg players: Player)
        = onlinePlayerSetOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg players: Player)
        = plugin.onlinePlayerSetOf(*players)

fun onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: RacciPlugin)
        = OnlinePlayerSet(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun RacciPlugin.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = onlinePlayerSetOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = plugin.onlinePlayerSetOf(*pair)

// Map

fun <V> RacciPlugin.onlinePlayerMapOf() = OnlinePlayerMap<V>(this)

fun <V> WithPlugin<*>.onlinePlayerMapOf() = plugin.onlinePlayerMapOf<V>()

fun <V> onlinePlayerMapOf(vararg pair: Pair<Player, V>, plugin: RacciPlugin)
        = OnlinePlayerMap<V>(plugin).apply { putAll(pair) }

fun <V> RacciPlugin.onlinePlayerMapOf(vararg pair: Pair<Player, V>)
        = onlinePlayerMapOf(*pair, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg pair: Pair<Player, V>)
        = plugin.onlinePlayerMapOf(*pair)

fun <V> onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>, plugin: RacciPlugin)
        = OnlinePlayerMap<V>(plugin).apply { triple.forEach { (player, value, whenPlayerQuit) -> put(player, value, whenPlayerQuit) } }

fun <V> RacciPlugin.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>)
        = onlinePlayerMapOf(*triple, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>)
        = plugin.onlinePlayerMapOf(*triple)

class OnlinePlayerList(override val plugin: RacciPlugin) : LinkedList<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableList<Pair<Player, WhenPlayerQuitCollectionCallback>> = LinkedList()

    override fun add(player: Player, whenPlayerQuitCallback: Player.() -> Unit): Boolean {
        return if(super<OnlinePlayerCollection>.add(player, whenPlayerQuitCallback)) {
            whenQuit.add(player to whenPlayerQuitCallback)
            true
        } else false
    }

    override fun add(element: Player): Boolean {
        if (isEmpty()) registerEvents(plugin)
        return super<LinkedList>.add(element)
    }

    override fun quit(player: Player): Boolean {
        return if(super.quit(player)) {
            val iterator = whenQuit.iterator()
            for (pair in iterator) {
                if(pair.first == player) {
                    iterator.remove()
                    pair.second.invoke(pair.first)
                }
            }
            true
        } else false
    }

    override fun removeFirst(): Player {
        return super.removeFirst().also {
            checkRegistration()
        }
    }

    override fun removeLastOccurrence(p0: Any?): Boolean {
        return super.removeLastOccurrence(p0).also {
            if(it) checkRegistration()
        }
    }

    override fun removeAt(p0: Int): Player {
        return super.removeAt(p0).also {
            checkRegistration()
        }
    }

    override fun remove(element: Player): Boolean {
        return if(super.remove(element)) {
            checkRegistration()
            true
        } else false
    }

    override fun removeLast(): Player {
        return super.removeLast().also {
            checkRegistration()
        }
    }
}

class OnlinePlayerSet(override val plugin: RacciPlugin) : HashSet<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableMap<Player, WhenPlayerQuitCollectionCallback> = mutableMapOf()

    override fun add(player: Player, whenPlayerQuitCallback: WhenPlayerQuitCollectionCallback): Boolean {
        return if(super<OnlinePlayerCollection>.add(player, whenPlayerQuitCallback)) {
            whenQuit[player] = whenPlayerQuitCallback

            checkRegistration()
            true
        } else false
    }

    override fun add(element: Player): Boolean {
        return super<HashSet>.add(element).also {
            if(it) checkRegistration()
        }
    }

    override fun remove(element: Player): Boolean {
        return super.remove(element).also {
            if(it) checkRegistration()
        }
    }

    override fun quit(player: Player): Boolean {
        return if(super.quit(player)) {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player)
            }
            true
        } else false
    }
}

interface OnlinePlayerCollection : MutableCollection<Player>, KListener<RacciPlugin> {

    fun checkRegistration() {
        if(size == 1) {
            event<PlayerQuitEvent> { quit(player) }
            event<PlayerKickEvent> { quit(player) }
        } else if(size == 0) {
            unregisterListener()
        }
    }

    /**
     * Adds a new Player to the collection with a callback for when the player quits the server.
     */
    fun add(player: Player, whenPlayerQuit: WhenPlayerQuitCollectionCallback): Boolean {
        return add(player).also {
            if(it) checkRegistration()
        }
    }

    /**
     * Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback] provided.
     */
    fun quit(player: Player): Boolean {
        return remove(player).also {
            if(it) checkRegistration()
        }
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

class OnlinePlayerMap<V>(override val plugin: RacciPlugin) : HashMap<Player, V>(), KListener<RacciPlugin> {
    private val whenQuit: HashMap<Player, WhenPlayerQuitMapCallback<V>> = hashMapOf()

    /**
     * Puts a Player to the map with a [value] and a callback for when the player quits the server.
     */
    fun put(key: Player, value: V, whenPlayerQuit: WhenPlayerQuitMapCallback<V>): V? {
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

    override fun remove(key: Player): V? {
        return super.remove(key).also {
            checkRegistration()
        }
    }

    override fun remove(key: Player, value: V): Boolean {
        return super.remove(key, value).also {
            checkRegistration()
        }
    }

    private fun checkRegistration() {
        if(size == 1) {
            event<PlayerQuitEvent> { quit(player) }
            event<PlayerKickEvent> { quit(player) }
        } else if(size == 0) {
            unregisterListener()
        }
    }
}