package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.koin.core.component.createScope
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.core.scope.get
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.findAnnotation

/** Global platform independent extension. */
public abstract class PlatformIndependentExtension<P : MinixPlugin> internal constructor() : ExtensionSkeleton<P> {
    final override val value: String = buildString {
        append(plugin.value)
        append(':')
        append(
            this@PlatformIndependentExtension::class.findAnnotation<MappedExtension>()?.name
                ?.takeUnless { it == MappedExtension.REPLACE_ME }
                ?: this@PlatformIndependentExtension::class.simpleName ?: error("No name in annotation and extension anonymous.")
        )
    }

    final override val scope: Scope = createScope(value)

    final override val eventListener: PlatformListener<P> by lazy { PlatformListener(plugin) }

    final override var state: ExtensionState = ExtensionState.UNLOADED
        set(value) { get<FlowBus>().post(ExtensionStateEvent(this.castOrThrow(), this.state, state)); field = value }

    final override val dispatcher: Closeable<ExecutorCoroutineDispatcher> = scope.get(parameters = { parametersOf(this) })

    final override val supervisor: CoroutineScope by lazy {
        scope.get<CoroutineExceptionHandler>(parameters = { parametersOf(this, value) })
            .let(::CoroutineScope)
            .plus(SupervisorJob(plugin.coroutineScope as Job)) // Supervisor job is a child of the plugin supervisor.
            .plus(dispatcher.get()) // Dispatcher for the execution context
    }

    final override fun launch(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[this.plugin].launch(this.dispatcher.get(), this.supervisor, block = block)

    final override fun async(
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[this.plugin].launch(this.dispatcher.get(), this.supervisor, block = block)

    final override fun toString(): String = "Extension(name=$value, state=$state)"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Extension<*>) return false

        return plugin === other.plugin && value == other.value && state == other.state
    }

    final override fun hashCode(): Int {
        var result = plugin.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + supervisor.hashCode()
        result = 31 * result + dispatcher.hashCode()
        return result
    }
}
