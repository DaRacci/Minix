package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.annotations.DoNotUnload
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.integrations.IntegrationManager
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.core.services.IntegrationService
import io.github.classgraph.ClassInfo
import org.koin.core.component.get
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@DoNotUnload
@MappedExtension(Minix::class, "Integration Mapper", [IntegrationService::class])
class IntegrationMapper : MapperService(
    Integration::class,
    MappedIntegration::class
) {
    override suspend fun registerMapped(
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

        IntegrationService.IntegrationLoader(annotation.pluginName) {
            val constructor = kClass.primaryConstructor!!
            when (constructor.parameters.size) {
                0 -> constructor.call()
                else -> constructor.call(plugin)
            }
        }.let(get<IntegrationService>()::registerIntegration)
    }

    override suspend fun forgetMapped(
        plugin: MinixPlugin,
        cache: PluginData<*>
    ) {
        val service = get<IntegrationService>()
        service.integrations
            .filter { it.value.loaded }
            .filter { it.value.get().getOrThrow().plugin == plugin }
            .onEach { service.integrations.unregister(it.key) }
            .forEach { service.integrations.remove(it.key) }
    }
}
