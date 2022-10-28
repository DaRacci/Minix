package dev.racci.minix.integrations

import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.integrations.annotations.MappedIntegration
import org.apiguardian.api.API
import java.util.Optional
import kotlin.reflect.full.findAnnotation

@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
public open class IntegrationManager<I : Integration> {
    @PublishedApi
    internal val integrations: MutableSet<IntegrationRef<I>> = mutableSetOf()

    /** Registers an Integration */
    @Throws(MissingAnnotationException::class)
    public fun register(integration: I) {
        val annotation = integration::class.findAnnotation<MappedIntegration>() ?: throw MissingAnnotationException(integration::class, MappedIntegration::class)
        val ref = IntegrationRef(integration, annotation.pluginName)

        MANAGERS.add(this) // Only registers managers if they have registered an integration.

        this.integrations.removeIf { oldIntegration -> oldIntegration.pluginName == ref.pluginName }
        this.integrations.add(ref)
    }

    /** Runs the action of the first found registered integration */
    public fun onFirstRegistered(action: (I) -> Unit) {
        if (this.integrations.isEmpty()) return

        this.integrations.first().let { action(it.ref) }
    }

    /** Runs the action of the first found registered integration and returns an optional of [T]. */
    public fun <T : Any> getFirstRegistered(action: (I) -> T): Optional<T> {
        var result: T? = null
        this.onFirstRegistered { integration -> result = action(integration) }

        return Optional.ofNullable(result)
    }

    public inline fun <reified T : Integration> isRegistered(): Boolean = this.integrations
        .filterIsInstance<T>()
        .isNotEmpty()

    public companion object : IntegrationManager<Integration>() {
        internal val MANAGERS: MutableSet<IntegrationManager<*>> = mutableSetOf()
    }
}
