package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.Closeable
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.companionParent
import dev.racci.minix.api.utils.now
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext
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

    final override val value get() = name

    final override var state = ExtensionState.UNLOADED

    final override val loaded get() = state < ExtensionState.UNLOADED

    final override val supervisor by lazy { CoroutineScope(SupervisorJob()) }

    /** This extensions local isolated thread context. */
    override val dispatcher = object : Closeable<ExecutorCoroutineDispatcher>() {
        override fun create(): ExecutorCoroutineDispatcher {
            val threadCount = this@Extension::class.findAnnotation<MappedExtension>()!!.threadCount
            return newFixedThreadPoolContext(threadCount, "${name.substringAfter(':')}-Thread")
        }

        override fun onClose() {
            value.value?.close()
        }
    }

    final override val eventListener = object : KListener<P> {
        override val plugin: P get() = this@Extension.plugin
    }

    final override val name by lazy {
        buildString {
            append(plugin.name)
            append(':')
            append(this@Extension::class.findAnnotation<MappedExtension>()!!.name)
        }
    }

    suspend fun setState(state: ExtensionState) {
        send(plugin, ExtensionStateEvent(this, state))
        this.state = state
    }

    final override fun launch(
        dispatcher: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = pluginService.coroutineSession[plugin].launch(dispatcher, this.supervisor, block = block)

    final override fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = pluginService.coroutineSession[plugin].launch(plugin.minecraftDispatcher, this.supervisor, block = block)

    final override fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = pluginService.coroutineSession[plugin].launch(this.dispatcher.get(), this.supervisor, block = block)

    final override fun toString(): String = "Extension(name='$name', state='$state')"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Extension<*>) return false

        if (name != other.name) return false
        if (state != other.state) return false
        if (supervisor != other.supervisor) return false
        if (dispatcher != other.dispatcher) return false
        if (eventListener != other.eventListener) return false

        return true
    }

    final override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + supervisor.hashCode()
        result = 31 * result + dispatcher.hashCode()
        result = 31 * result + eventListener.hashCode()
        return result
    }

    /**
     * Designed to be applied to a companion object of a class extending [Extension].
     * This will allow a static method for getting the service or
     * injecting it.
     *
     * ## Note: If used incorrectly it will throw [ClassCastException] when
     * used.
     *
     * @param E The type of the extension. (The class that extends [Extension])
     * @see [DataService.Companion]
     */
    abstract class ExtensionCompanion<E : Extension<*>> : KoinComponent {
        private val cached: AtomicRef<Pair<E, Instant>> by lazy { atomic(Pair(getKoin().get(getParent()), now())) }

        operator fun getValue(thisRef: ExtensionCompanion<E>, property: KProperty<*>): E = thisRef.getService()

        fun getService(): E {
            cached.update { (extension, ts) ->
                if (ts + 5.seconds < now()) {
                    Pair(getKoin().get(getParent()), now())
                } else {
                    Pair(extension, ts)
                }
            }

            return cached.value.first
        }

        fun inject(): Lazy<E> = lazy { getKoin().get(getParent()) }

        private fun getParent() = this::class.companionParent.castOrThrow<KClass<Extension<*>>>()
    }

    private companion object {
        private val pluginService by getKoin().inject<PluginService>()
    }
}
