package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.core.services.DataServiceImpl
import io.github.classgraph.ClassInfo
import org.koin.core.component.get
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException

@MappedExtension(Minix::class, "Configuration Mapper", [DataService::class])
class ConfigurationMapper(override val plugin: Minix) : MapperService(
    MinixConfig::class,
    MappedConfig::class
) {
    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        try {
            get<DataServiceImpl>().configDataHolder[classInfo.loadClass().kotlin.castOrThrow()]
        } catch (e: NoBeanDefFoundException) {
            logger.error(e) { "Failed to register config [${plugin.name}:${classInfo.name}]" }
        } catch (e: Exception) {
            throw logger.fatal(e) { "Failed to create configuration [${plugin.name}:${classInfo.name}]" }
        }
    }

    override suspend fun forgetMapped(
        plugin: MinixPlugin,
        cache: PluginData<*>
    ) {
        val dataService = get<DataServiceImpl>()

        cache.configurations
            .onEach(dataService.configDataHolder::invalidate)
            .onEach { unloadKoinModules(KoinUtils.getModule(it)) }
            .forEach(KoinUtils::clearBinds)
    }
}
