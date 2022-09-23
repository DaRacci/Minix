package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.services.DataServiceImpl
import io.github.classgraph.ClassInfo
import org.koin.core.error.NoBeanDefFoundException

@MappedConfig(Minix::class, "Configuration Mapper")
class ConfigurationMapper : MapperService(
    MinixConfig::class,
    MappedConfig::class
) {
    override fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        try {
            DataServiceImpl.getService().configDataHolder.get(classInfo.loadClass().kotlin.castOrThrow())
        } catch (e: NoBeanDefFoundException) {
            plugin.log.error(e) { "Failed to register config [${plugin.name}:${classInfo.name}]" }
        } catch (e: Exception) {
            throw plugin.log.fatal(e) { "Failed to create configuration [${plugin.name}:${classInfo.name}]" }
        }
    }
}
