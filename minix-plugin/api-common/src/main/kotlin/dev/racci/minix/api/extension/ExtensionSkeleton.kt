package dev.racci.minix.api.extension

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.orElse
import dev.racci.minix.api.annotations.MappedExtension
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
import org.koin.core.component.createScope
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.core.scope.Scope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.findAnnotation

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
     * The bound dispatcher for sub coroutines. If thread count in
     * [MappedExtension] is -1, then this will instead return the plugins'
     * dispatcher.
     */
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
    public val state: ExtensionState

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

        /** Creates a new QualifierValue for the extension, such as `minix:pluginservice` */
        internal fun valueFor(thisRef: ExtensionSkeleton<*>): Lazy<QualifierValue> = lazy {
            buildString {
                append(thisRef.plugin.value.lowercase())
                append(':')
                Option.fromNullable(thisRef::class.findAnnotation<MappedExtension>()?.name)
                    .filterNot { it === MappedExtension.REPLACE_ME }
                    .orElse { Option.fromNullable(thisRef::class.simpleName) }
                    .getOrElse { error("No name in annotation and extension was anonymous.") }
                    .let(::append)
            }
        }

        internal fun scopeFor(thisRef: ExtensionSkeleton<*>): Lazy<Scope> = lazy {
            thisRef.createScope(this).also { innerScope -> innerScope.linkTo(thisRef.plugin.scope) }
        }
    }
}
