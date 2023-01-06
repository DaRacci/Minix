package dev.racci.minix.api.collections.expiring

import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.ApiStatus
import java.util.WeakHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun <E> expirationListOf(
    backgroundContext: CoroutineContext = Dispatchers.Default
): ExpirationCollection<E> = ExpirationListImpl(backgroundContext)

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun <E> expirationListOf(
    expireIn: Duration = Duration.INFINITE,
    vararg elements: E
): ExpirationCollection<E> = expirationListOf<E>().also { it.addAll(expireIn, *elements) }

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun <K, V> expirationMapOf(
    defaultDuration: Duration = Duration.INFINITE,
    backgroundContext: CoroutineContext = Dispatchers.Default,
    vararg entries: Pair<K, Triple<V, Duration, ExpirationMapCallback<K, V>?>>
): ExpirationMap<K, V> = ExpirationMapImpl<K, V>(defaultDuration, backgroundContext, WeakHashMap(entries.size)).also { map ->
    entries.forEach { (k, t) -> map.put(k, t.first, t.second, t.third) }
}

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
@JvmName("expirationMapOfPairDuration")
public fun <K, V> expirationMapOf(
    defaultDuration: Duration = Duration.INFINITE,
    backgroundContext: CoroutineContext = Dispatchers.Default,
    vararg entries: Pair<K, Pair<V, Duration>>
): ExpirationMap<K, V> = expirationMapOf(defaultDuration, backgroundContext, *entries.map { (k, p) -> k to Triple(p.first, p.second, null) }.toTypedArray())

@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
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
