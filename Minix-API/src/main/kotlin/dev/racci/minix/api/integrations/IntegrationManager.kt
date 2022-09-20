package dev.racci.minix.api.integrations

import dev.racci.minix.api.annotations.MinixInternal
import org.apiguardian.api.API
import java.util.Optional

@[API(status = API.Status.EXPERIMENTAL, since = "4.0.0") OptIn(MinixInternal::class)]
open class IntegrationManager<I : Integration> {
    @MinixInternal val REGISTERED: MutableSet<I> = mutableSetOf()

    @MinixInternal val UNREGISTERED: MutableSet<I> = mutableSetOf()

    /** Registers an Integration */
    fun register(integration: I) {
        MANAGERS.add(this) // Only registers managers if they have registered an integration.

        this.REGISTERED.removeIf { oldIntegration -> oldIntegration.pluginName == integration.pluginName }
        this.REGISTERED.add(integration)
    }

    /** Runs the action of the first found registered integration */
    fun onFirstRegistered(action: (I) -> Unit) {
        if (this.REGISTERED.isEmpty()) return

        this.REGISTERED.first().let(action)
    }

    /** Runs the action of the first found registered integration and returns an optional of [T]. */
    fun <T : Any> getFirstRegistered(action: (I) -> T): Optional<T> {
        var result: T? = null
        this.onFirstRegistered { integration -> result = action(integration) }

        return Optional.ofNullable(result)
    }

    inline fun <reified T : Integration> isRegistered(): Boolean = this.REGISTERED
        .filterIsInstance<T>()
        .isNotEmpty()

    companion object {
        internal val MANAGERS: MutableSet<IntegrationManager<*>> = mutableSetOf()
    }
}
