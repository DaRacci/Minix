package dev.racci.minix.api.extension

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.kotlin.companionParent
import dev.racci.minix.api.utils.now
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration.Companion.seconds

/**
 * Utility class for access an extension via its companion object.
 * If your extension has a defined interface you should use that instead.
 *
 * @param E The type of the extension. (The class that extends [Extension])
 * @throws IllegalArgumentException If the not a companion object or not companion to an extension.
 * @throws ClassCastException If the companion parent is not of type [E]
 */
public abstract class ExtensionCompanion<E : Extension<*>> : KoinComponent {
    private val cached: AtomicRef<Pair<E, Instant>> by lazy { atomic(Pair(getKoin().get(getParent()), now())) }

    public operator fun getValue(thisRef: ExtensionCompanion<E>, property: KProperty<*>): E = thisRef.getService()

    public operator fun getValue(thisRef: Any?, property: Any?): E = getService()

    public fun getService(): E {
        cached.update { (extension, ts) ->
            if (ts + 5.seconds < now()) {
                Pair(getKoin().get(getParent()), now())
            } else {
                Pair(extension, ts)
            }
        }

        return cached.value.first
    }

    public fun inject(): Lazy<E> = lazy { getKoin().get(getParent()) }

    private fun getParent() = this::class.companionParent.castOrThrow<KClass<E>>()

    init {
        require(this::class.isCompanion) { "ExtensionCompanion must be used on a companion object." }
        require(this::class.companionParent!!.isSubclassOf(Extension::class)) { "ExtensionCompanion must be used on a companion object of an extension." }
    }
}
