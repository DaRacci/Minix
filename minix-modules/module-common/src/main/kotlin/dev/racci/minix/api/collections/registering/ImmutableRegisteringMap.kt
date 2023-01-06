package dev.racci.minix.api.collections.registering

import dev.racci.minix.api.lifecycles.Loadable
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentHashMap
import org.jetbrains.annotations.ApiStatus

/**
 * An Immutable variant of an [RegisteringMap].
 *
 * @param K The key type in the map.
 * @param V The value type in the map.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public open class ImmutableRegisteringMap<K : Any, V : Any> internal constructor(
    protected override val internalMap: PersistentMap<K, Loadable<V>>
) : RegisteringMap<K, V>() {

    public companion object {
        /**
         * @param K The key type in the map.
         * @param V The value type in the map.
         * @param entries The pairs that will represent the maps register-able elements.
         * @return A new [ImmutableRegisteringMap] with no elements.
         */
        public fun <K : Any, V : Any> of(
            vararg entries: Pair<K, Loadable<V>>
        ): ImmutableRegisteringMap<K, V> = ImmutableRegisteringMap(persistentHashMapOf(*entries))

        /**
         * @param K The key type in the map.
         * @param V The value type in the map.
         * @param entries The pairs that will represent the maps register-able elements.
         * @return A new [ImmutableRegisteringMap] with the given pairs inserted.
         */
        public fun <K : Any, V : Any> of(
            entries: Iterable<Pair<K, Loadable<V>>>
        ): ImmutableRegisteringMap<K, V> = ImmutableRegisteringMap(entries.toMap().toPersistentHashMap())
    }
}
