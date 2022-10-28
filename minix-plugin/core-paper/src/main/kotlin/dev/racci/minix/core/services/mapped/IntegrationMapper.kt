package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.data.IntegrationLoader
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.IntegrationService
import dev.racci.minix.integrations.Integration
import dev.racci.minix.integrations.IntegrationManager
import dev.racci.minix.integrations.annotations.MappedIntegration
import io.github.classgraph.ClassInfo
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

public actual class IntegrationMapper : MapperService<Minix>, Extension<Minix>() {
    override val plugin: Minix by inject()
    override val superclass: KClass<out Any> = Integration::class
    override val targetAnnotation: KClass<out Annotation> = MappedIntegration::class

    actual override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<out Integration>>()
        val annotation = kClass.findAnnotation<MappedIntegration>()!!
        val managerKClass = annotation.integrationManager

        when {
            managerKClass == IntegrationManager::class -> plugin.log.trace { "Integration [${kClass.simpleName}] is self-acting." }
            managerKClass.objectInstance.safeCast<IntegrationManager<out Integration>>() == null -> return plugin.log.error { "Failed to obtain singleton instance of ${managerKClass.qualifiedName}." }
        }

        IntegrationLoader(annotation.pluginName) {
            val constructor = kClass.primaryConstructor!!
            when (constructor.parameters.size) {
                0 -> constructor.call()
                else -> constructor.call(plugin)
            }
        }.let(get<IntegrationService>()::registerIntegration)
    }

    actual override suspend fun forgetMapped(plugin: MinixPlugin) {
        val service = get<IntegrationService>()
        service.integrations.entries
            .filter { it.value.plugin == plugin }
            .onEach { service.integrations.unregister(it.key) }
            .forEach { service.integrations.remove(it.key) }
    }
}
