package dev.racci.minix.api.collections.expiring

import kotlinx.coroutines.Dispatchers
import java.util.WeakHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

public fun <E> expirationListOf(
    backgroundContext: CoroutineContext = Dispatchers.Default
): ExpirationCollection<E> = ExpirationListImpl(backgroundContext)

public fun <E> expirationListOf(
    expireIn: Duration = Duration.INFINITE,
    vararg elements: E
): ExpirationCollection<E> = expirationListOf<E>().also { it.addAll(expireIn, *elements) }

public fun <E> expirationListOf(
    expireIn: Duration = Duration.INFINITE,
    vararg elements: Pair<E, ExpirationCallback<E>>
): ExpirationCollection<E> = expirationListOf<E>().also { it.addAll(expireIn, *elements) }

public fun <K, V> expirationMapOf(
    defaultDuration: Duration = Duration.INFINITE,
    backgroundContext: CoroutineContext = Dispatchers.Default,
    vararg entries: Pair<K, Triple<V, Duration, ExpirationMapCallback<K, V>?>>
): ExpirationMap<K, V> = ExpirationMapImpl<K, V>(defaultDuration, backgroundContext, WeakHashMap(entries.size)).also { map ->
    entries.forEach { (k, t) -> map.put(k, t.first, t.second, t.third) }
}

@JvmName("expirationMapOfPairDuration")
public fun <K, V> expirationMapOf(
    defaultDuration: Duration = Duration.INFINITE,
    backgroundContext: CoroutineContext = Dispatchers.Default,
    vararg entries: Pair<K, Pair<V, Duration>>
): ExpirationMap<K, V> = expirationMapOf(defaultDuration, backgroundContext, *entries.map { (k, p) -> k to Triple(p.first, p.second, null) }.toTypedArray())

@JvmName("expirationMapOfPairCallback")
public fun <K, V> expirationMapOf(
    defaultDuration: Duration = Duration.INFINITE,
    backgroundContext: CoroutineContext = Dispatchers.Default,
    vararg entries: Pair<K, Pair<V, ExpirationMapCallback<K, V>?>>
): ExpirationMap<K, V> = expirationMapOf(
    defaultDuration,
    backgroundContext,
    *entries.map { (k, p) -> k to Triple(p.first, defaultDuration, p.second) }.toTypedArray()
)
