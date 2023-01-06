package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
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
import org.koin.core.component.createScope
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/** Global platform independent extension. */
public abstract class PlatformIndependentExtension<P : MinixPlugin> internal constructor() : ExtensionSkeleton<P> {
    final override val plugin: P = koin.get(
        this::class.supertypes[0]
            .arguments[0]
            .type!!
            .classifier
            .castOrThrow<KClass<P>>()
    )

    final override val value: String = buildString {
        append(plugin.value)
        append(':')
        append(
            this@PlatformIndependentExtension::class.findAnnotation<MappedExtension>()?.name
                ?.takeUnless { it == MappedExtension.REPLACE_ME }
                ?: this@PlatformIndependentExtension::class.simpleName ?: error("No name in annotation and extension anonymous.")
        )
    }

    final override val eventListener: PlatformListener<P> by lazy { PlatformListener(plugin) }

    final override val logger: MinixLogger by MinixLoggerFactory

    final override var state: ExtensionState = ExtensionState.UNLOADED
        set(value) {
            get<FlowBus>().post(ExtensionStateEvent(this.castOrThrow(), state, value))
            field = value
        }

    final override val dispatcher: Closeable<ExecutorCoroutineDispatcher> = get(parameters = { parametersOf(this) })

    final override val supervisor: CoroutineScope = get<CoroutineExceptionHandler>(parameters = { parametersOf(this, value) })
        .let(::CoroutineScope)
        .plus(SupervisorJob(plugin.coroutineScope as Job)) // Supervisor job is a child of the plugin supervisor.
        .plus(dispatcher.get()) // Dispatcher for the execution context

    // Must be after all properties used in hashCode because it is used in the constructor.
    final override val scope: Scope = createScope(value).also { innerScope -> innerScope.linkTo(plugin.scope) }

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
