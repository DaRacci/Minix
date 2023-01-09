package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.core.data.IntegrationLoader
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.IntegrationService
import dev.racci.minix.integrations.Integration
import dev.racci.minix.integrations.IntegrationManager
import dev.racci.minix.integrations.annotations.IntegrationPlugin
import io.github.classgraph.ClassInfo
import org.koin.core.component.get
import org.koin.core.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import dev.racci.minix.integrations.annotations.IntegrationManager as IntegrationManagerAnnotation

public actual class IntegrationMapper : MapperService<Minix, Integration>() {
    actual override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin,
        module: Module
    ) {
        val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<out Integration>>()
        val managerKClass = kClass.findAnnotation<IntegrationManagerAnnotation>()?.kClass
        val integrationPlugin = kClass.findAnnotation<IntegrationPlugin>()?.pluginName ?: throw MissingAnnotationException(kClass, IntegrationPlugin::class)

        when {
            managerKClass == null || managerKClass == IntegrationManager::class -> plugin.logger.trace { "Integration [${kClass.simpleName}] is self-acting." }
            managerKClass.objectInstance.safeCast<IntegrationManager<out Integration>>() == null -> return plugin.logger.error { "Failed to obtain singleton instance of ${managerKClass.qualifiedName}." }
        }

        IntegrationLoader(integrationPlugin) {
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
            .filter { entry -> get<PluginService>().fromClass(entry.value::class).exists { it == plugin } }
            .onEach { service.integrations.unregister(it.key) }
            .forEach { service.integrations.remove(it.key) }
    }
}
