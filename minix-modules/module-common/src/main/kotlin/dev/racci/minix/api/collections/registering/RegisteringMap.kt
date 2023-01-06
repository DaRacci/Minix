package dev.racci.minix.api.collections.registering

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.getOrNone
import dev.racci.minix.api.lifecycles.Loadable
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public sealed class RegisteringMap<K : Any, V : Any> {
    protected abstract val internalMap: Map<K, Loadable<V>>

    public val entries: ImmutableSet<Map.Entry<K, V>>
        get() = getRegistered().entries

    /** An [ImmutableSet] of the keys in this map. */
    public val keys: ImmutableSet<K>
        get() = internalMap.keys.toImmutableSet()

    /** An [ImmutableCollection] of the registered values. */
    public val values: ImmutableCollection<V>
        get() = getRegistered().values

    /** The size of this map. */
    public val size: Int
        get() = this.internalMap.size

    /** @returns The value of a given key if the key is present, and the value is registered. */
    public operator fun get(key: K): V? = this.internalMap[key]?.get()?.getOrNull()

    /** @returns True if the given key is present in this map. */
    public fun containsKey(key: K): Boolean = this.internalMap.containsKey(key)

    /** @returns True if the given [Loadable] value is registered in this map. */
    public fun containsValue(value: Loadable<V>): Boolean = this.internalMap.containsValue(value)

    /** @returns True if the given value is registered in this map. */
    public fun containsValue(value: V): Boolean = this.internalMap.values.any { it.get().isSuccess && it.get().getOrThrow() == value }

    /**
     * @return An [ImmutableMap] of all the currently registered entries.
     */
    public fun getRegistered(): ImmutableMap<K, V> = this.internalMap.entries
        .filterNot { (_, value) -> value.loaded }
        .associate { (key, value) -> key to value.get().getOrThrow() }
        .toImmutableMap()

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
    public fun register(descriptor: K): Result<V> = internalMap.getOrNone(descriptor)
        .map { loadable ->
            if (loadable.loaded) {
                Result.failure(IllegalStateException("Descriptor $descriptor is already registered."))
            } else loadable.get(false)
        }.getOrElse { Result.failure(IllegalStateException("Descriptor $descriptor is not registered.")) }

    /**
     * Attempts to register all still unregistered values.
     *
     * @return An [ImmutableSet] containing all values that were successfully registered.
     */
    public suspend fun tryRegisterAll(): ImmutableSet<V> = internalMap.values
        .filter(Loadable<V>::loaded)
        .mapNotNull { it.load(false).getOrNull() }
        .toImmutableSet()

    /**
     * Attempts to register the value with the given descriptor.
     *
     * If the descriptor isn't present, it will return a failed [Result].
     * If the descriptor isn't registered, it will return a failed [Result].
     *
     * @param descriptor The descriptor to unregister the value with.
     * @return If the value was successfully unregistered.
     */
    public suspend fun unregister(descriptor: K): Boolean = internalMap.getOrNone(descriptor)
        .filterNot(Loadable<V>::loaded)
        .tap { it.unload() }
        .fold({ false }, { true })

    /**
     * Unregisters all registered values.
     *
     * @returns An [ImmutableSet] containing all the keys that got unregistered.
     */
    public suspend fun unregisterAll(): ImmutableSet<K> = this.internalMap.entries
        .filterNot { (_, value) -> value.loaded }
        .onEach { (_, value) -> value.unload() }
        .map { (key, _) -> key }.toImmutableSet()

    /**
     * Attempts to get the value with the given descriptor.
     *
     * @param descriptor The descriptor to get the value with.
     * @return An [Option] containing the value if it is registered, or None if it was not.
     */
    public fun getRegistered(descriptor: K): Option<V> = internalMap.getOrNone(descriptor)
        .filterNot(Loadable<V>::loaded)
        .map { it.get(false).getOrThrow() }
}
