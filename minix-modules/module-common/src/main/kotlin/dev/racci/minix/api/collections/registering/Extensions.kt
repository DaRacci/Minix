package dev.racci.minix.api.collections.registering // ktlint-disable filename

import dev.racci.minix.api.lifecycles.Loadable
import org.jetbrains.annotations.ApiStatus

/**
 * @param K The type of the key.
 * @param V The type of the value.
 * @return A new [MutableRegisteringMap] with no elements.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun <K : Any, V : Any> registeringMapOf(): MutableRegisteringMap<K, V> = MutableRegisteringMap.of()

/**
 * @param K The key type in the map.
 * @param V The value type in the map.
 * @param pairs The pairs to insert into the map.
 * @return A new [MutableRegisteringMap] with the given pairs inserted.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun <K : Any, V : Any> registeringMapOf(
    vararg pairs: Pair<K, Loadable<V>>
): MutableRegisteringMap<K, V> = MutableRegisteringMap.of(*pairs)
