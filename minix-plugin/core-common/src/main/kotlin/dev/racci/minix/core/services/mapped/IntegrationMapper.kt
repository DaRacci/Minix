package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.annotations.DoNotUnload
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.IntegrationService
import io.github.classgraph.ClassInfo

@DoNotUnload
@MappedExtension([IntegrationService::class])
public expect class IntegrationMapper : MapperService<Minix> {
    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    )

    override suspend fun forgetMapped(plugin: MinixPlugin)
}
