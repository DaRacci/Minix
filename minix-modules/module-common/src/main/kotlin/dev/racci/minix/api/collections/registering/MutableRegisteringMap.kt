package dev.racci.minix.api.collections.registering

import dev.racci.minix.api.extensions.collections.computeAndRemove
import dev.racci.minix.api.lifecycles.Loadable
import org.apiguardian.api.API

/**
 * A collection that contains registrable values
 */
// TODO -> Concurrent version
@API(status = API.Status.MAINTAINED, since = "5.0.0")
public open class MutableRegisteringMap<K : Any, V : Any> private constructor(
    override val internalMap: MutableMap<K, Loadable<V>>
) : RegisteringMap<K, V>() {

    /**
     * Puts a new entry to this map.
     *
     * @param descriptor The descriptor of the entry.
     * @param loader The loader of the entry.
     * @param unload The optional function to unload the entry.
     * @return True if the entry was added, false if the entry already exists.
     */
    public suspend fun put(
        descriptor: K,
        loader: suspend () -> V,
        unload: suspend V.() -> Unit = {}
    ): Boolean {
        if (internalMap.containsKey(descriptor)) return false

        internalMap[descriptor] = object : Loadable<V>() {
            override suspend fun onLoad() = loader()
            override suspend fun onUnload(value: V) = value.unload()
        }

        return true
    }

    /**
     * Puts a loadable entry to this map.
     *
     * @see put
     */
    public fun put(
        descriptor: K,
        loadable: Loadable<V>
    ): Boolean {
        if (internalMap.containsKey(descriptor)) return false

        internalMap[descriptor] = loadable

        return true
    }

    /**
     * Removes an entry from this map.
     * If this entry is registered, it will be unloaded.
     *
     * @param descriptor The descriptor of the entry.
     * @return True if the entry was removed, false if the entry does not exist.
     */
    public fun remove(descriptor: K): Boolean {
        return internalMap.computeAndRemove(descriptor, Loadable<V>::unload)
    }
}
