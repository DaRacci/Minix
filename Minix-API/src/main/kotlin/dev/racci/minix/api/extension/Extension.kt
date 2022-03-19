package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

abstract class Extension<P : MinixPlugin> : KoinComponent, Qualifier, WithPlugin<P> {
    private val annotation by lazy { this::class.findAnnotation<MappedExtension>() }

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

    inline fun <reified R> runSync(crossinline block: suspend () -> R): R {
        var result: Result<R> = Result.failure(RuntimeException("Error while running sync block"))
        plugin.launch { result = Result.success(block()) }
        return result.getOrThrow()
    }

    inline fun <reified R> runAsync(crossinline block: suspend () -> R): R {
        var result: Result<R> = Result.failure(RuntimeException("Error while running sync block"))
        plugin.launchAsync { result = Result.success(block()) }
        return result.getOrThrow()
    }

    inline fun <reified T : () -> R, reified R> T.runSync(): R = runSync(this)

    inline fun <reified T : () -> R, reified R> T.runAsync(): R = runAsync(this)

    final override fun toString(): String {
        return "${plugin.name}:$value"
    }
}
