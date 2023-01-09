package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.flowbus.FlowBus
import dev.racci.minix.flowbus.receiver.EventReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import org.apiguardian.api.API
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.createScope
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.core.scope.Scope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

public interface ExtensionSkeleton<P : MinixPlugin> :
    Qualifier,
    EventReceiver,
    WithPlugin<P>,
    ComplexManagedLifecycle {

    /** The scope of this extension, linked to the plugins main scope. */
    public override val scope: Scope

    /**
     * The qualified name of the extension This will be in the format of
     * “pluginId:extensionId”.
     */
    public override val value: QualifierValue

    /**
     * The bound dispatcher for sub coroutines.
     * If [dev.racci.minix.api.annotations.Threads] is not present, or equal to 0,
     * then this will instead return the plugins' dispatcher.
     */
    // TODO: Lazy delegate
    public val dispatcher: Closeable<ExecutorCoroutineDispatcher>

    /**
     * The supervisor job for sub coroutines. This will be cancelled when the
     * extension is unloaded, or the parent plugin is unloaded. The default
     * context for this supervisor is [dispatcher]
     */
    public val supervisor: CoroutineScope

    /** The Listener, which is used to register events in the extension. */
    @get:API(status = API.Status.INTERNAL)
    public val eventListener: PlatformListener<P>

    /**
     * The current state of the extension. You can listen to state changes on
     * [FlowBus] with the event type of [ExtensionStateEvent].
     */
    @set:ApiStatus.Internal
    public var state: ExtensionState

    /**
     * Launch a coroutine on the extensions threads.
     *
     * ## Note this override ignores the [context] parameter.
     */
    override fun launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.scope.get<CoroutineSession>().launch(dispatcher.get(), this.supervisor, block = block)

    public companion object {

        /**
         * Creates a new QualifierValue for the extension,
         * This name will be in the format of “pluginId:extensionId”.
         *
         * The pluginId will be the lowercase value of [MinixPlugin.value],
         * The extensionId will be the extensions simple class-name transformed like `PluginServiceImpl` -> `plugin-service`.
         */
        internal fun valueFor(thisRef: ExtensionSkeleton<*>): Lazy<QualifierValue> = lazy {
            buildString {
                append(thisRef.plugin.value.lowercase())
                append(':')

                val rawName = thisRef::class.simpleName ?: error("Anonymous extension classes aren't supported. `$thisRef`")
                rawName.removeSuffix("Impl").forEachIndexed { i, c ->
                    if (i != 0 && c.isUpperCase()) append('-')
                    append(c.lowercase())
                }
            }
        }

        internal fun scopeFor(thisRef: ExtensionSkeleton<*>): Lazy<Scope> = lazy {
            thisRef.createScope(this).also { innerScope -> innerScope.linkTo(thisRef.plugin.scope) }
        }
    }

    public object Comparator : kotlin.Comparator<KClass<out ExtensionSkeleton<*>>> {
        override fun compare(
            o1: KClass<out ExtensionSkeleton<*>>,
            o2: KClass<out ExtensionSkeleton<*>>
        ): Int {
            // If classes are the same, return equal.
            if (o1 == o2) return 0

            // If the other extension depends on this one, then this one should be loaded first.
            if (Depends.dependsOn(o2, o1)) return -1

            // If this extension depends on the other one, then this one should be loaded last.
            if (Depends.dependsOn(o1, o2)) return 1

            // Otherwise, they can be loaded in any order, return 1 to push to end.
            return 1
        }
    }
}
