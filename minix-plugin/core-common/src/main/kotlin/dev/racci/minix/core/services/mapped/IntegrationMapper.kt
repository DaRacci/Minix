package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.annotations.Required
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.IntegrationService
import dev.racci.minix.integrations.Integration
import io.github.classgraph.ClassInfo

@Required
@Depends([IntegrationService::class])
public expect class IntegrationMapper : MapperService<Minix, Integration> {
    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    )

    override suspend fun forgetMapped(plugin: MinixPlugin)
}
