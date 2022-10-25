package dev.racci.minix.api.collections.player

import dev.racci.minix.api.callbacks.PlayerQuitMapCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.player.WrappedPlayerQuitEvent
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.flowbus.receiver.EventReceiver
import dev.racci.minix.flowbus.subscribe
import org.apiguardian.api.API

@API(status = API.Status.MAINTAINED, since = "5.0.0")
public class OnlinePlayerMap<V> internal constructor(
    private val backingMap: MutableMap<MinixPlayer, Pair<V, PlayerQuitMapCallback<V>?>>
) : Map<MinixPlayer, V>, EventReceiver by getKoin().get() {

    override val entries: Set<Map.Entry<MinixPlayer, V>> get() = this.backingMap.entries.map { Entry(it.key, it.value.first) }.toSet()
    override val keys: Set<MinixPlayer> get() = this.backingMap.keys
    override val size: Int get() = this.backingMap.size
    override val values: Collection<V> get() = this.backingMap.values.map { it.first }

    override fun containsKey(key: MinixPlayer): Boolean = this.backingMap.containsKey(key)

    override fun containsValue(value: V): Boolean = this.backingMap.values.any { it.first == value }

    override fun get(key: MinixPlayer): V? = this.backingMap[key]?.first

    override fun isEmpty(): Boolean = this.backingMap.isEmpty()

    public fun put(
        key: MinixPlayer,
        value: V,
        onQuit: PlayerQuitMapCallback<V> = PlayerQuitMapCallback.empty.castOrThrow()
    ): V? = this.backingMap.put(key, value to onQuit).also { this.checkRegistration() }?.first

    public fun quit(player: MinixPlayer) {
        this.backingMap.remove(player)?.also { (value, callback) ->
            callback?.invoke(player, value)
            this.checkRegistration()
        }
    }

    public fun clearQuiting() {
        keys.toMutableList().forEach(::quit)
    }

    public fun remove(key: MinixPlayer): V? = this.backingMap.remove(key)?.also { this.checkRegistration() }?.first

    public fun remove(
        key: MinixPlayer,
        value: V
    ): Boolean = this.backingMap.remove(key, value to null).also { this.checkRegistration() }

    private fun checkRegistration() {
        if (size == 1) {
            this.subscribe<WrappedPlayerQuitEvent> { event ->
                this.quit(event.player)
            }
        } else if (isEmpty()) {
            this.unsubscribe()
        }
    }

    internal class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>
}
