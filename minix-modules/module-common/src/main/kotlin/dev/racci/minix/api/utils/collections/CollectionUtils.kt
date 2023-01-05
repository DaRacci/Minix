package dev.racci.minix.api.utils.collections

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.Validated
import arrow.core.filterIsInstance
import arrow.core.toOption
import dev.racci.minix.api.extensions.reflection.castOrThrow
import org.apiguardian.api.API
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KCallable

/**
 * Utilities for Generic Collections.
 */
@API(status = API.Status.MAINTAINED, since = "4.2.0")
public object CollectionUtils {
    public object If {
        @OptIn(ExperimentalContracts::class)
        @JvmName("ifEmptyCollection")
        public suspend inline fun <R, C : Collection<*>> ifEmpty(
            collection: C,
            crossinline action: suspend C.() -> R
        ): Option<R> {
            contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
            return if (!collection.isEmpty()) Some(action(collection)) else None
        }

        @OptIn(ExperimentalContracts::class)
        @JvmName("ifEmptyArray")
        public suspend inline fun <R, T> ifEmpty(
            array: Array<T>,
            crossinline action: suspend Array<T>.() -> R
        ): Option<R> {
            contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
            return if (array.isEmpty()) Some(action(array)) else None
        }

        @OptIn(ExperimentalContracts::class)
        @JvmName("ifNotEmptyCollection")
        public suspend inline fun <R, C : Collection<*>> ifNotEmpty(
            collection: C,
            crossinline action: suspend C.() -> R
        ): Option<R> {
            contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
            return if (collection.isNotEmpty()) Some(action(collection)) else None
        }

        @OptIn(ExperimentalContracts::class)
        @JvmName("ifNotEmptyArray")
        public suspend inline fun <R, T> ifNotEmpty(
            array: Array<T>,
            crossinline action: suspend Array<T>.() -> R
        ): Option<R> {
            contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
            return if (array.isNotEmpty()) Some(action(array)) else None
        }
    }

    public object Contains {
        @JvmName("containsStringCollection")
        public fun containsString(
            collection: Collection<String>,
            value: String,
            ignoreCase: Boolean
        ): Boolean = collection.any { it.equals(value, ignoreCase) }

        @JvmName("containsStringArray")
        public fun containsString(
            collection: Array<String>,
            value: String,
            ignoreCase: Boolean
        ): Boolean = collection.any { it.equals(value, ignoreCase) }
    }

    public object Get {
        @JvmName("getCastCollection")
        public fun <T : Any> getCast(
            collection: Collection<*>,
            index: Int
        ): Validated<Throwable, T> = Validated.catch { collection.elementAt(index).castOrThrow() }

        @JvmName("getCastArray")
        public fun <T : Any> getCast(
            collection: Array<*>,
            index: Int
        ): Validated<Throwable, T> = Validated.catch { collection.elementAt(index).castOrThrow() }
    }

    public object Find {
        @JvmName("findKCallableCollection")
        public fun <T : KCallable<*>> findKCallable(
            collection: Collection<T>,
            name: String,
            ignoreCase: Boolean
        ): Option<T> = collection.find { it.name.equals(name, ignoreCase) }.toOption()

        @JvmName("findKCallableArray")
        public fun <T : KCallable<*>, R> findKCallable(
            collection: Array<T>,
            name: String,
            ignoreCase: Boolean
        ): Option<KCallable<R>> = collection.find { it.name.equals(name, ignoreCase) }.toOption().filterIsInstance()
    }

    public object Mutate {
        @JvmName("clearCollection")
        public suspend inline fun <T> clear(
            collection: MutableCollection<T>,
            crossinline action: suspend T.() -> Unit
        ): Unit = collection.forEach { item ->
            item.action()
            collection.remove(item)
        }
    }
}
