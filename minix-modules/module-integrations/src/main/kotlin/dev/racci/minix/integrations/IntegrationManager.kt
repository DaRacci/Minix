package dev.racci.minix.integrations

import org.apiguardian.api.API
import java.util.Optional

@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
public open class IntegrationManager<I : Integration> {
    @PublishedApi
    internal val REGISTERED: MutableSet<I> = mutableSetOf()

    @PublishedApi
    internal val UNREGISTERED: MutableSet<I> = mutableSetOf()

    /** Registers an Integration */
    public fun register(integration: I) {
        MANAGERS.add(this) // Only registers managers if they have registered an integration.

        this.REGISTERED.removeIf { oldIntegration -> oldIntegration.pluginName == integration.pluginName }
        this.REGISTERED.add(integration)
    }

    /** Runs the action of the first found registered integration */
    public fun onFirstRegistered(action: (I) -> Unit) {
        if (this.REGISTERED.isEmpty()) return

        this.REGISTERED.first().let(action)
    }

    /** Runs the action of the first found registered integration and returns an optional of [T]. */
    public fun <T : Any> getFirstRegistered(action: (I) -> T): Optional<T> {
        var result: T? = null
        this.onFirstRegistered { integration -> result = action(integration) }

        return Optional.ofNullable(result)
    }

    public inline fun <reified T : Integration> isRegistered(): Boolean = this.REGISTERED
        .filterIsInstance<T>()
        .isNotEmpty()

    public companion object : IntegrationManager<Integration>() {
        internal val MANAGERS: MutableSet<IntegrationManager<*>> = mutableSetOf()
    }
}
