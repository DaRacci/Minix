package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.companionParent
import dev.racci.minix.api.utils.unsafeCast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableDeferred
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

/**
 * An Extension is a class which is designed to basically act like it's own mini plugin.
 * With dependencies for other extensions and load states.
 *
 * @param P The owning plugin.
 * @see DataService
 */
abstract class Extension<P : MinixPlugin> : KoinComponent, Qualifier, WithPlugin<P> {
    private val annotation by lazy { this::class.findAnnotation<MappedExtension>() }
    private val pluginService by inject<PluginService>()

    open val name: String get() = annotation?.name ?: this::class.simpleName ?: throw RuntimeException("Extension name is not defined")

    open val bindToKClass: KClass<*>? get() = annotation?.bindToKClass.takeIf { it != Extension::class }

    open val minix by inject<Minix>()

    open val log get() = plugin.log

    open val dependencies: ImmutableList<KClass<out Extension<*>>> get() = annotation?.dependencies?.filterIsInstance<KClass<Extension<*>>>().orEmpty().toImmutableList() // This will be worse for performance but save on memory

    open var state: ExtensionState = ExtensionState.UNLOADED

    open val loaded: Boolean get() = state == ExtensionState.LOADED || state == ExtensionState.ENABLED

    override val value by lazy(::name)

    @MinixInternal
    var bound: Boolean = false

    /** Called when the plugin loading and not yet enabled. */
    open suspend fun handleLoad() {}

    /** Called when the plugin has finished loading and is enabled. */
    open suspend fun handleEnable() {}

    /** Called when the plugin is being disabled. */
    open suspend fun handleUnload() {}

    open suspend fun setState(state: ExtensionState) {
        minix.send(ExtensionStateEvent(this, state))
        this.state = state
    }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.sync(suspend () -> R)", "dev.racci.minix.api.extension.sync"))
    inline fun <reified R> sync(crossinline block: suspend () -> R) { plugin.launch() { block() } }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.async(suspend () -> R)", "dev.racci.minix.api.extension.async"))
    inline fun <reified R> async(crossinline block: suspend () -> R) { plugin.launchAsync { block() } }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.completableSync(suspend () -> R)", "dev.racci.minix.api.extension.completableSync"))
    inline fun <reified R> completableSync(crossinline block: suspend () -> R): CompletableFuture<R> {
        val future = CompletableFuture<R>()
        plugin.launch { future.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            future.cancel(true)
        }
        return future
    }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.completableAsync(suspend () -> R)", "dev.racci.minix.api.extension.completableAsync"))
    inline fun <reified R> completableAsync(crossinline block: suspend () -> R): CompletableFuture<R> {
        val future = CompletableFuture<R>()
        plugin.launchAsync { future.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            future.cancel(true)
        }
        return future
    }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.deferredSync(suspend () -> R)", "dev.racci.minix.api.extension.deferredSync"))
    inline fun <reified R> deferredSync(crossinline block: suspend () -> R): CompletableDeferred<R> {
        val deferred = CompletableDeferred<R>()
        plugin.launch { deferred.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            deferred.cancel()
        }
        return deferred
    }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Moved to ExPlugin", ReplaceWith("WithPlugin<*>.deferredAsync(suspend () -> R)", "dev.racci.minix.api.extension.deferredAsync"))
    inline fun <reified R> deferredAsync(crossinline block: suspend () -> R): CompletableDeferred<R> {
        val deferred = CompletableDeferred<R>()
        plugin.launchAsync { deferred.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            deferred.cancel()
        }
        return deferred
    }

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.sync(suspend () -> Unit)", "dev.racci.minix.api.extension.sync"))
    inline fun <reified T : () -> R, reified R> T.sync() = sync(this)

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.async(suspend () -> Unit)", "dev.racci.minix.api.extension.async"))
    inline fun <reified T : () -> R, reified R> T.async() = async(this)

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.completableSync(suspend () -> Unit)", "dev.racci.minix.api.extension.completableSync"))
    inline fun <reified T : () -> R, reified R> T.completableSync(): CompletableFuture<R> = completableSync(this)

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.completableAsync(suspend () -> Unit)", "dev.racci.minix.api.extension.completableAsync"))
    inline fun <reified T : () -> R, reified R> T.completableAsync(): CompletableFuture<R> = completableAsync(this)

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.deferredSync(suspend () -> Unit)", "dev.racci.minix.api.extension.deferredSync"))
    inline fun <reified T : () -> R, reified R> T.deferredSync(): CompletableDeferred<R> = deferredSync(this)

    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated("Removed in 3.0.0", ReplaceWith("WithPlugin<*>.deferredAsync(suspend () -> Unit)", "dev.racci.minix.api.extension.deferredAsync"))
    inline fun <reified T : () -> R, reified R> T.deferredAsync(): CompletableDeferred<R> = deferredAsync(this)

    final override fun toString(): String {
        return "${plugin.name}:$value"
    }

    /**
     * Designed to be applied to a companion object of a class that extends [Extension].
     * This will allow a static method for getting the service or injecting it.
     * ## Note: If used incorrectly it will throw [ClassCastException] when used.
     * @param E The type of the extension. (The class that extends [Extension])
     * @see [DataService.Companion]
     */
    abstract class ExtensionCompanion<E : Extension<*>> {

        operator fun getValue(thisRef: ExtensionCompanion<E>, property: KProperty<*>): E = thisRef.getService()

        fun getService(): E {
            val parentClass = this::class.companionParent.unsafeCast<KClass<Extension<*>>>() // Will throw if implemented incorrectly
            return getKoin().get(parentClass)
        }

        fun inject(): Lazy<E> = lazy {
            val parentClass = this::class.companionParent.unsafeCast<KClass<Extension<*>>>() // Will throw if implemented incorrectly
            getKoin().get(parentClass)
        }
    }
}
