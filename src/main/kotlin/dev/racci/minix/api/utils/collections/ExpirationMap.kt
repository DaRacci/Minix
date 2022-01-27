@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineTask
import dev.racci.minix.api.utils.now
import java.util.WeakHashMap

typealias OnExpireMapCallback<K, V> = (K, V) -> Unit

fun <K, V> MinixPlugin.expirationMapOf(): ExpirationMap<K, V> = ExpirationMapImpl(this)

fun <K, V> WithPlugin<*>.expirationMapOf() = plugin.expirationMapOf<K, V>()

fun <K, V> expirationMapOf(
    expireTime: Long,
    plugin: MinixPlugin,
    vararg elements: Pair<K, V>,
) = plugin.expirationMapOf<K, V>().apply { elements.forEach { (key, value) -> put(key, value, expireTime) } }

fun <K, V> MinixPlugin.expirationMapOf(
    expireTime: Long,
    vararg elements: Pair<K, V>,
) = expirationMapOf(expireTime, this, elements = elements)

fun <K, V> WithPlugin<*>.expirationMapOf(
    expireTime: Long,
    vararg elements: Pair<K, V>,
) = plugin.expirationMapOf(expireTime, *elements)

fun <K, V> expirationMapOf(
    expireTime: Long,
    plugin: MinixPlugin,
    vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>,
) = plugin.expirationMapOf<K, V>()
    .apply { elements.forEach { (key, value, onExpire) -> put(key, value, expireTime, onExpire) } }

fun <K, V> MinixPlugin.expirationMapOf(
    expireTime: Long,
    vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>,
) = expirationMapOf(expireTime, this, elements = elements)

fun <K, V> WithPlugin<*>.expirationMapOf(
    expireTime: Long,
    vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>,
) = plugin.expirationMapOf(expireTime, *elements)

interface ExpirationMap<K, V> : MutableMap<K, V>, WithPlugin<MinixPlugin> {

    /**
     * Returns the missing time on seconds to expire the key,
     * -1 if was not specified the expiration time before(permanent key) or
     * `null` if the key is not contained in the map.
     */
    fun missingTime(key: K): Long?

    /**
     * Set expiration time to the key and returns `true` if the key is found
     * or false if the key is not contained in the map.
     */
    fun expire(
        key: K,
        time: Long,
    ): Boolean

    /**
     * Set expiration time to the key and returns `true` if the key is found
     * or false if the key is not contained in the map.
     *
     * [time] in seconds.
     * [callback] is called when the key expires.
     */
    fun expire(
        key: K,
        time: Long,
        callback: OnExpireMapCallback<K, V>,
    ): Boolean

    /**
     * Associates the specified [value] with the specified [key] in the map
     * with an expiration time.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    fun put(
        key: K,
        value: V,
        time: Long,
    ): V?

    /**
     * Associates the specified [value] with the specified [key] in the map
     * with an expiration time.
     *
     * [time] in seconds.
     * [callback] is called when the key expires.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    fun put(
        key: K,
        value: V,
        time: Long,
        callback: OnExpireMapCallback<K, V>,
    ): V?
}

class ExpirationMapImpl<K, V>(
    override val plugin: MinixPlugin,
    val initialMap: MutableMap<K, V> = WeakHashMap(),
) : ExpirationMap<K, V>, MutableMap<K, V> by initialMap {

    private val putTime: MutableMap<K, Long> = mutableMapOf()
    private val expiration: MutableMap<K, Long> = mutableMapOf()
    private val whenExpire: MutableMap<K, OnExpireMapCallback<K, V>> = mutableMapOf()
    private var task: CoroutineTask? = null
    private var emptyCount: Byte = 0

    override fun missingTime(key: K): Long? {
        return if (containsKey(key)) {
            (
                expiration[key]
                    ?: return -1
                ) - (now().toEpochMilliseconds() - putTime.getOrPut(key) { now().toEpochMilliseconds() } / 1000)
        } else null
    }

    private fun exp(
        key: K,
        time: Long,
    ) {
        putTime[key] = now().toEpochMilliseconds()
        expiration[key] = time
    }

    private fun whenEx(
        key: K,
        callback: OnExpireMapCallback<K, V>,
    ) {
        whenExpire[key] = callback
    }

    override fun expire(
        key: K,
        time: Long,
    ): Boolean = if (containsKey(key)) {
        exp(key, time)
        true
    } else false

    override fun expire(
        key: K,
        time: Long,
        callback: OnExpireMapCallback<K, V>,
    ): Boolean = if (expire(key, time)) {
        whenEx(key, callback)
        true
    } else false

    override fun put(
        key: K,
        value: V,
    ): V? {
        val result = initialMap.put(key, value)
        generateTask()
        return result
    }

    override fun put(
        key: K,
        value: V,
        time: Long,
    ): V? {
        require(time <= 0) { "expiration time can't be negative or zero" }
        val result = put(key, value)
        exp(key, time)
        return result
    }

    override fun put(
        key: K,
        value: V,
        time: Long,
        callback: OnExpireMapCallback<K, V>,
    ): V? {
        require(time <= 0) { "expiration time can't be negative or zero" }
        val result = put(key, value)
        exp(key, time)
        whenEx(key, callback)
        return result
    }

    override fun remove(key: K): V? {
        val result = initialMap.remove(key)
        if (result != null) {
            putTime.remove(key)
            expiration.remove(key)
            whenExpire.remove(key)
        }
        return result
    }

    private fun checkTime(
        current: Long,
        key: K,
    ) = current - putTime.getOrPut(key) { current } / 1000 - (expiration[key] ?: 0) >= 0

    private fun generateTask() {
        if (task != null) return
        task = scheduler {
            if (isEmpty()) {
                emptyCount++
            } else {
                emptyCount = 0
                val current = now().toEpochMilliseconds()
                for ((key, value) in entries) {
                    if (checkTime(current, key)) {
                        whenExpire.remove(key)?.invoke(key, value)
                        initialMap.remove(key)
                        putTime.remove(key)
                        expiration.remove(key)
                    }
                }
            }
            if (emptyCount > 5) {
                cancel()
                task = null
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
}
