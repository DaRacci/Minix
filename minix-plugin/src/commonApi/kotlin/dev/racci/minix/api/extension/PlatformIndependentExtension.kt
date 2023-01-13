package dev.racci.minix.api.extension

import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.typeArgumentOf
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.api.utils.koin
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.QualifierValue
import org.koin.core.scope.Scope
import kotlin.coroutines.CoroutineContext

/** Global platform independent extension. */
public abstract class PlatformIndependentExtension<P : MinixPlugin> internal constructor() : ExtensionSkeleton<P>, WithPlugin<P> {
    final override val plugin: P = koin.get(typeArgumentOf<PlatformIndependentExtension<*>, P>())

    // TODO: Is leaking here safe?
    final override val scope: Scope by ExtensionSkeleton.scopeFor(this)

    final override val value: QualifierValue by ExtensionSkeleton.valueFor(this)

    final override val logger: MinixLogger by MinixLoggerFactory.lazy

    final override val eventListener: PlatformListener<P> by lazy { PlatformListener(plugin) }

    final override var state: ExtensionState = ExtensionState.UNLOADED
        set(value) {
            get<FlowBus>().post(ExtensionStateEvent(this.castOrThrow(), state, value))
            field = value
        }

    final override val dispatcher: Closeable<ExecutorCoroutineDispatcher> by lazy {
        scope.get(parameters = { parametersOf(this) })
    }

    final override val supervisor: CoroutineScope by lazy {
        scope.get<CoroutineExceptionHandler>(parameters = { parametersOf(plugin, value) })
            .let(::CoroutineScope)
            .plus(SupervisorJob(plugin.coroutineSession.scope.coroutineContext[Job.Key] as Job)) // Supervisor job is a child of the plugin supervisor.
            .plus(dispatcher.get()) // Dispatcher for the execution context
    }

    final override fun launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.scope.get<CoroutineSession>().launch(this.dispatcher.get(), this.supervisor, block = block)

    final override fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = this.scope.get<CoroutineSession>().launch(this.dispatcher.get(), this.supervisor, block = block)

    public fun <T> Flow<T>.launchIn() {
        this.launchIn(supervisor)
    }

    final override fun toString(): String = "Extension(name=$value, state=$state)"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Extension<*> || other::class != this::class) return false

        return plugin === other.plugin && value == other.value
    }

    final override fun hashCode(): Int {
        var result = plugin.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}
