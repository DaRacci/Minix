package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableDeferred
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

abstract class Extension<P : MinixPlugin> : KoinComponent, Qualifier, WithPlugin<P> {
    private val annotation by lazy { this::class.findAnnotation<MappedExtension>() }
    private val pluginService by inject<PluginService>()

    open val name: String get() = annotation?.name ?: this::class.simpleName ?: this::class.simpleName ?: throw RuntimeException("Extension name is not defined")

    override val value: QualifierValue by lazy { name }

    open val bindToKClass: KClass<*>? get() = annotation?.bindToKClass.takeIf { it != Extension::class }

    open val minix by inject<Minix>()

    open val log get() = plugin.log

    open val dependencies: ImmutableList<KClass<out Extension<*>>> get() = annotation?.dependencies?.filterIsInstance<KClass<Extension<*>>>().orEmpty().toImmutableList() // This will be worse for performance but save on memory

    open var state: ExtensionState = ExtensionState.UNLOADED

    open val loaded: Boolean get() = state == ExtensionState.LOADED || state == ExtensionState.ENABLED

    @ApiStatus.Internal
    var bound: Boolean = false

    open suspend fun handleLoad() {}

    open suspend fun handleEnable() {}

    open suspend fun setState(state: ExtensionState) {
        minix.send(ExtensionStateEvent(this, state))
        this.state = state
    }

    open suspend fun handleUnload() {}

    inline fun <reified R> sync(crossinline block: suspend () -> R) { plugin.launch() { block() } }

    inline fun <reified R> async(crossinline block: suspend () -> R) { plugin.launchAsync { block() } }

    inline fun <reified R> completableSync(crossinline block: suspend () -> R): CompletableFuture<R> {
        val future = CompletableFuture<R>()
        plugin.launch { future.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            future.cancel(true)
        }
        return future
    }

    inline fun <reified R> completableAsync(crossinline block: suspend () -> R): CompletableFuture<R> {
        val future = CompletableFuture<R>()
        plugin.launchAsync { future.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            future.cancel(true)
        }
        return future
    }

    inline fun <reified R> deferredSync(crossinline block: suspend () -> R): CompletableDeferred<R> {
        val deferred = CompletableDeferred<R>()
        plugin.launch { deferred.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            deferred.cancel()
        }
        return deferred
    }

    inline fun <reified R> deferredAsync(crossinline block: suspend () -> R): CompletableDeferred<R> {
        val deferred = CompletableDeferred<R>()
        plugin.launchAsync { deferred.complete(block()) }.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            deferred.cancel()
        }
        return deferred
    }

    inline fun <reified T : () -> R, reified R> T.sync() = sync(this)

    inline fun <reified T : () -> R, reified R> T.async() = async(this)

    inline fun <reified T : () -> R, reified R> T.completableSync(): CompletableFuture<R> = completableSync(this)

    inline fun <reified T : () -> R, reified R> T.completableAsync(): CompletableFuture<R> = completableAsync(this)

    inline fun <reified T : () -> R, reified R> T.deferredSync(): CompletableDeferred<R> = deferredSync(this)

    inline fun <reified T : () -> R, reified R> T.deferredAsync(): CompletableDeferred<R> = deferredAsync(this)

    final override fun toString(): String {
        return "${plugin.name}:$value"
    }
}
