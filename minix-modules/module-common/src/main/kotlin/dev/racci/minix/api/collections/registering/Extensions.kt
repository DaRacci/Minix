package dev.racci.minix.api.collections.registering // ktlint-disable filename

import dev.racci.minix.api.lifecycles.Loadable

public fun <K : Any, V : Any> registeringMapOf(): MutableRegisteringMap<K, V> = MutableRegisteringMap.of()

public fun <K : Any, V : Any> registeringMapOf(vararg pairs: Pair<K, Loadable<V>>): MutableRegisteringMap<K, V> = MutableRegisteringMap.of(*pairs)
