package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.utils.Closeable
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.companionParent
import dev.racci.minix.api.utils.now
import dev.racci.minix.api.utils.unsafeCast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.datetime.Instant
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.time.Duration.Companion.seconds

/**
 * An Extension is a class, which is designed to basically act like it's own mini plugin.
 * With dependencies for other extensions and load states.
 *
 * @param P The owning plugin.
 * @see DataService
 */
@OptIn(MinixInternal::class, DelicateCoroutinesApi::class)
abstract class Extension<P : MinixPlugin> : ExtensionSkeleton<P> {

    final override var state = ExtensionState.UNLOADED

    /** This extensions local isolated thread context. */
    override val dispatcher = object : Closeable<ExecutorCoroutineDispatcher>() {
        override fun create(): ExecutorCoroutineDispatcher {
            val threadCount = this@Extension::class.findAnnotation<MappedExtension>()!!.threadCount
            return newFixedThreadPoolContext(threadCount, "$name-thread")
        }

        override fun onClose() {
            value.value?.close()
        }
    }

    final override val eventListener = object : KListener<P> {
        override val plugin: P get() = this@Extension.plugin
    }

    final override val loaded get() = state == ExtensionState.LOADED || state == ExtensionState.ENABLED

    final override val name by lazy {
        buildString {
            append(plugin.name)
            append(':')
            append(this@Extension::class.findAnnotation<MappedExtension>()!!.name)
        }
    }

    final override val supervisor by lazy { CoroutineScope(SupervisorJob()) }

    final override val value get() = name

    suspend fun setState(state: ExtensionState) {
        send(plugin, ExtensionStateEvent(this, state))
        this.state = state
    }

    final override fun toString(): String = "${plugin.name}:$value"

    /**
     * Designed to be applied to a companion object of a class extending
     * [Extension]. This will allow a static method for getting the service or
     * injecting it.
     *
     * ## Note: If used incorrectly it will throw [ClassCastException] when
     * used.
     *
     * @param E The type of the extension. (The class that extends [Extension])
     * @see [DataService.Companion]
     */
    abstract class ExtensionCompanion<E : Extension<*>> {
        private var cached: Pair<E, Instant>? = null

        operator fun getValue(thisRef: ExtensionCompanion<E>, property: KProperty<*>): E = thisRef.getService()

        fun getService(): E {
            if (cached == null || (cached!!.second + 5.seconds) < now()) {
                cached = Pair(getKoin().get(getParent()), now())
            }
            return cached!!.first
        }

        fun inject(): Lazy<E> = lazy { getKoin().get(getParent()) }

        private fun getParent() = this::class.companionParent.unsafeCast<KClass<Extension<*>>>()
    }
}
