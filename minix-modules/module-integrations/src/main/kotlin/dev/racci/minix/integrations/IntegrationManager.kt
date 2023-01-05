package dev.racci.minix.integrations

import arrow.core.Option
import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.integrations.annotations.MappedIntegration
import org.apiguardian.api.API
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

    public fun get(): Option<I> = integrations.firstOrNull().let { Option.fromNullable(it?.ref) }

    public inline fun <reified T : Integration> isRegistered(): Boolean = this.integrations
        .filterIsInstance<T>()
        .isNotEmpty()

    public companion object : IntegrationManager<Integration>() {
        internal val MANAGERS: MutableSet<IntegrationManager<*>> = mutableSetOf()
    }
}
