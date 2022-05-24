package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.SimpleKListener
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.inWholeTicks
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.companionParent
import dev.racci.minix.api.utils.unsafeCast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.minecraft.world.entity.ai.memory.ExpirableValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.time.Duration.Companion.seconds

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
    @MinixInternal val eventListener by lazy { SimpleKListener(plugin) }
    @MinixInternal val supervisor by lazy { CoroutineScope(SupervisorJob()) }

    open val name: String get() = annotation?.name ?: this::class.simpleName ?: throw RuntimeException("Extension name is not defined")

    open val bindToKClass: KClass<*>? get() = annotation?.bindToKClass.takeIf { it != Extension::class }

    open val minix by inject<Minix>()

    open val log get() = plugin.log

    open val dependencies: ImmutableList<KClass<out Extension<*>>> get() = annotation?.dependencies?.filterIsInstance<KClass<Extension<*>>>().orEmpty().toImmutableList()

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
        send(plugin, ExtensionStateEvent(this, state))
        this.state = state
    }

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
        private var cached: ExpirableValue<E>? = null

        operator fun getValue(thisRef: ExtensionCompanion<E>, property: KProperty<*>): E = thisRef.getService()

        fun getService(): E {
            val parentClass = this::class.companionParent.unsafeCast<KClass<Extension<*>>>() // Will throw if implemented incorrectly
            if (cached == null || cached!!.hasExpired()) {
                cached = ExpirableValue.of(getKoin().get<E>(parentClass), 15.seconds.inWholeTicks)
            }
            return cached!!.value
        }

        fun inject(): Lazy<E> = lazy {
            val parentClass = this::class.companionParent.unsafeCast<KClass<Extension<*>>>() // Will throw if implemented incorrectly
            getKoin().get(parentClass)
        }
    }
}
