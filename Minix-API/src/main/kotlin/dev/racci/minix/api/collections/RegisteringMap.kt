package dev.racci.minix.api.collections

import dev.racci.minix.api.extensions.collections.computeAndRemove
import dev.racci.minix.api.utils.Loadable
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import org.apiguardian.api.API
import java.util.Optional

/**
 * A collection that contains registrable values
 */
@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
class RegisteringMap<D : Any, R : Any> : Map<D, Loadable<R>> {
    private val internalMap = mutableMapOf<D, Loadable<R>>()

    override val entries = internalMap.entries.toImmutableSet()
    override val keys = internalMap.keys.toImmutableSet()
    override val size = internalMap.size
    override val values = internalMap.values.toImmutableSet()

    override fun containsKey(key: D) = internalMap.containsKey(key)
    override fun containsValue(value: Loadable<R>) = internalMap.containsValue(value)
    override fun get(key: D) = internalMap[key]
    override fun isEmpty() = this.internalMap.isEmpty()

    /**
     * Puts a new entry to this map.
     *
     * @param descriptor The descriptor of the entry.
     * @param loader The loader of the entry.
     * @param unload The optional function to unload the entry.
     * @return True if the entry was added, false if the entry already exists.
     */
    suspend fun put(
        descriptor: D,
        loader: suspend () -> R,
        unload: suspend R.() -> Unit = {}
    ): Boolean {
        if (internalMap.containsKey(descriptor)) return false

        internalMap[descriptor] = object : Loadable<R>() {
            override suspend fun onLoad() = loader()
            override suspend fun onUnload(value: R) = value.unload()
        }

        return true
    }

    /**
     * Puts a loadable entry to this map.
     *
     * @see put
     */
    fun put(
        descriptor: D,
        loadable: Loadable<R>
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
    suspend fun remove(descriptor: D): Boolean {
        return internalMap.computeAndRemove(descriptor) { this.unload() }
    }

    /** @return All registered values in this map. */
    fun getRegistered(): ImmutableSet<R> {
        val set = mutableSetOf<R>()
        for (value in internalMap.values) {
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
    fun register(descriptor: D): Result<R> {
        val loadable = internalMap[descriptor] ?: return Result.failure(IllegalStateException("Descriptor $descriptor is not registered."))
        if (loadable.loaded) return Result.failure(IllegalStateException("Descriptor $descriptor is already registered."))

        return loadable.get(false)
    }

    suspend fun registerAll() {
        for (value in internalMap.values) {
            if (value.loaded) continue
            println("Registering $value")

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
    suspend fun unregister(descriptor: D): Boolean {
        val loadable = internalMap[descriptor] ?: return false
        if (loadable.unloaded) return false

        return loadable.unload()
    }

    /** Unregisters all registered values. */
    suspend fun unregisterAll() {
        for (value in internalMap.values) {
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
    fun getRegistered(descriptor: D): Optional<R> {
        val loadable = internalMap[descriptor] ?: return Optional.empty()
        if (loadable.unloaded) return Optional.empty()

        return Optional.of(loadable.get(false).getOrThrow())
    }
}
