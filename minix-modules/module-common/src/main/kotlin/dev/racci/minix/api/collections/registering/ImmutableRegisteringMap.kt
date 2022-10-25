package dev.racci.minix.api.collections.registering

import dev.racci.minix.api.lifecycles.Loadable
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentHashMap

public open class ImmutableRegisteringMap<K : Any, V : Any> internal constructor(
    protected override val internalMap: PersistentMap<K, Loadable<V>>
) : RegisteringMap<K, V>() {

    public companion object {
        public fun <K : Any, V : Any> of(
            vararg entries: Pair<K, Loadable<V>>
        ): ImmutableRegisteringMap<K, V> = ImmutableRegisteringMap(persistentHashMapOf(*entries))

        public fun <K : Any, V : Any> of(
            entries: Iterable<Pair<K, Loadable<V>>>
        ): ImmutableRegisteringMap<K, V> = ImmutableRegisteringMap(entries.toMap().toPersistentHashMap())
    }
}
