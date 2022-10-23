package dev.racci.minix.api.collections.expiring

import dev.racci.minix.api.utils.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.WeakHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

public class ExpirationMapImpl<K, V>(
    private val defaultDuration: Duration = Duration.INFINITE,
    private val backgroundContext: CoroutineContext = Dispatchers.Default,
    private val backingMap: MutableMap<K, ExpirationMapValue<K, V>> = WeakHashMap()
) : ExpirationMap<K, V>, MutableMap<K, V> {
    private var builtTicker: ReceiveChannel<Unit>? = null
    private var emptyCount: Byte = 0

    override val size: Int by backingMap::size

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = backingMap.entries
        .map { ExpirationEntry(it.key, it.value.value) }.toMutableSet()

    override val keys: MutableSet<K> = backingMap.keys

    override val values: MutableCollection<V> = backingMap.values.mapTo(
        ArrayList(backingMap.values.size),
        ExpirationMapValue<K, V>::value
    )

    override operator fun get(key: K): V? = backingMap[key]?.value

    override fun containsKey(key: K): Boolean = backingMap.containsKey(key)

    override fun containsValue(value: V): Boolean = backingMap.values.any { it.value == value }

    override fun isEmpty(): Boolean = size == 0

    override fun clear(): Unit = backingMap.clear()

    override fun putAll(from: Map<out K, V>): Unit = from.forEach(::put)

    override fun remainingTime(key: K): Duration? {
        val data = this.backingMap[key] ?: return null
        return data.remainingTime()
    }

    override fun setExpiration(
        key: K,
        expireIn: Duration,
        onExpiration: ExpirationMapCallback<K, V>?
    ): Boolean {
        val data = this.backingMap[key] ?: return false
        this.backingMap[key] = data.copy(
            expireIn = expireIn,
            startTime = now(),
            onExpire = onExpiration ?: data.onExpire
        )

        return true
    }

    public override fun setExpirationCallback(
        key: K,
        onExpire: ExpirationMapCallback<K, V>?
    ): Boolean {
        val existing = backingMap[key] ?: return false
        backingMap[key] = existing.copy(onExpire = onExpire)

        return true
    }

    override fun put(
        key: K,
        value: V
    ): V? = backingMap.compute(key) { _, existing ->
        if (existing == null) generateTask()
        ExpirationMapValue(value, defaultDuration, now(), existing?.onExpire)
    }?.value

    public override fun put(
        key: K,
        value: V,
        expireIn: Duration
    ): V? = backingMap.compute(key) { _, existing ->
        if (existing == null) generateTask()
        ExpirationMapValue(value, expireIn, now(), existing?.onExpire)
    }?.value

    public override fun put(
        key: K,
        value: V,
        expireIn: Duration,
        callback: ExpirationMapCallback<K, V>?
    ): V? = backingMap.compute(key) { _, existing ->
        if (existing == null) generateTask()
        ExpirationMapValue(value, expireIn, now(), callback)
    }?.value

    override fun remove(key: K): V? = backingMap.remove(key)?.value

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun generateTask() {
        if (builtTicker != null) return

        builtTicker = ticker(
            delayMillis = 50,
            initialDelayMillis = 50,
            context = backgroundContext,
            mode = TickerMode.FIXED_DELAY
        )

        runBlocking {
            withContext(backgroundContext) {
                if (isEmpty()) {
                    emptyCount++
                } else {
                    emptyCount = 0
                    for ((key, value) in backingMap.entries) {
                        if (value.remainingTime() != Duration.ZERO) continue
                        value.onExpire?.onExpire(key, value.value)
                        backingMap.remove(key)
                    }
                }

                if (emptyCount > 5) {
                    builtTicker?.cancel()
                    builtTicker = null
                }
            }
        }
    }

    private inner class ExpirationEntry(
        override val key: K,
        override val value: V
    ) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            return backingMap.computeIfPresent(key) { _, data ->
                data.copy(value = newValue)
            }!!.value // This will come back to haunt me at some point
        }
    }
}
