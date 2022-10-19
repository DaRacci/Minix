package dev.racci.minix.api.collections.registering

import dev.racci.minix.api.lifecycles.Loadable
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import org.apiguardian.api.API
import java.util.Optional

@API(status = API.Status.MAINTAINED, since = "5.0.0")
public sealed class RegisteringMap<K : Any, V : Any> {
    protected abstract val internalMap: Map<K, Loadable<V>>

    public val entries: ImmutableSet<Entry<K, V>> get() {
        return this.internalMap.entries
            .filter { it.value.get().isSuccess }
            .map { Entry(it.key, it.value.get().getOrThrow()) }
            .toImmutableSet()
    }

    public val keys: ImmutableSet<K> get() = this.internalMap.keys.toImmutableSet()

    public val values: ImmutableSet<V> get() = this.getRegistered()

    public val size: Int get() = this.internalMap.size

    public fun containsKey(key: K): Boolean = this.internalMap.containsKey(key)

    public fun containsValue(value: Loadable<V>): Boolean = this.internalMap.containsValue(value)

    public fun containsValue(value: V): Boolean = this.internalMap.values.any { it.get().isSuccess && it.get().getOrThrow() == value }

    public fun get(key: K): V? = this.internalMap[key]?.get()?.getOrNull()

    /** @return All registered values in this map. */
    public fun getRegistered(): ImmutableSet<V> {
        val set = mutableSetOf<V>()
        for (value in this.internalMap.values) {
            if (value.unloaded) continue

            value.get(false).onSuccess(set::add)
        }

        return set.toImmutableSet()
    }

    /**
     * Attempts to register the value with the given descriptor.
     *
     * If the descriptor isn't present, it will return a failed [Result].
     * If the descriptor is already registered, it will return a failed [Result].
     * If the loadable fails to load the value, it will return a failed [Result] and will remain unregistered.
     *
     * @param descriptor The descriptor to register the value with.
     * @return A [Result] containing the value if it was successfully registered, or an error if it was not.
     */
    public fun register(descriptor: K): Result<V> {
        val loadable = this.internalMap[descriptor] ?: return Result.failure(IllegalStateException("Descriptor $descriptor is not registered."))
        if (loadable.loaded) return Result.failure(IllegalStateException("Descriptor $descriptor is already registered."))

        return loadable.get(false)
    }

    public suspend fun tryRegisterAll() {
        for (value in this.internalMap.values) {
            if (value.loaded) continue

            value.load(false)
        }
    }

    /**
     * Attempts to register the value with the given descriptor.
     *
     * If the descriptor isn't present, it will return a failed [Result].
     * If the descriptor isn't registered, it will return a failed [Result].
     *
     * @param descriptor The descriptor to unregister the value with.
     * @return If the value was successfully unregistered.
     */
    public suspend fun unregister(descriptor: K): Boolean {
        val loadable = this.internalMap[descriptor] ?: return false
        if (loadable.unloaded) return false

        return loadable.unload()
    }

    /** Unregisters all registered values. */
    public suspend fun unregisterAll() {
        for (value in this.internalMap.values) {
            if (value.unloaded) continue

            value.unload()
        }
    }

    /**
     * Attempts to get the value with the given descriptor.
     *
     * @param descriptor The descriptor to get the value with.
     * @return An [Optional] containing the value if it is registered, or an empty [Optional] if it was not.
     */
    public fun getRegistered(descriptor: K): Optional<V> {
        val loadable = this.internalMap[descriptor] ?: return Optional.empty()
        if (loadable.unloaded) return Optional.empty()

        return Optional.of(loadable.get(false).getOrThrow())
    }

    public class Entry<K : Any, V : Any> internal constructor(override val key: K, override val value: V) : Map.Entry<K, V>
}
