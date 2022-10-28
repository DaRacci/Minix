package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import org.apiguardian.api.API
import org.koin.core.component.KoinScopeComponent
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import kotlin.coroutines.CoroutineContext

public interface ExtensionSkeleton<P : MinixPlugin> : WithPlugin<P>, KoinScopeComponent, Qualifier, ComplexManagedLifecycle {

    /**
     * The qualified name of the extension
     * This will be in the format of “pluginId:extensionId”.
     */
    public override val value: QualifierValue

    /**
     * The bound dispatcher for sub coroutines.
     * If thread count in [MappedExtension] is -1, then this will instead return the plugins' dispatcher.
     */
    public val dispatcher: Closeable<ExecutorCoroutineDispatcher>

    /**
     * The supervisor job for sub coroutines.
     * This will be cancelled when the extension is unloaded, or the parent plugin is unloaded.
     * The default context for this supervisor is [dispatcher]
     */
    public val supervisor: CoroutineScope

    /** The Listener, which is used to register events in the extension. */
    @get:API(status = API.Status.INTERNAL)
    public val eventListener: PlatformListener<P>

    /** The name of the extension. */
    @Deprecated("Use value instead.", ReplaceWith("value"))
    public val name: String

    /**
     * The current state of the extension.
     * You can listen to state changes on [FlowBus] with the event type of [ExtensionStateEvent].
     */
    public val state: ExtensionState

    /**
     * Launch a coroutine on the extensions threads.
     * ## Note this override ignores the [context] parameter.
     */
    override fun launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[plugin].launch(dispatcher.get(), this.supervisor, block = block)
}
