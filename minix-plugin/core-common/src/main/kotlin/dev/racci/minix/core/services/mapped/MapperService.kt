package dev.racci.minix.core.services.mapped

import dev.racci.minix.api.autoscanner.Scanner
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.reflection.typeArgumentOf
import dev.racci.minix.api.plugin.MinixPlugin
import io.github.classgraph.ClassInfo
import org.jetbrains.annotations.ApiStatus.Experimental
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.reflect.KClass

/**
 * Simplifies the process of scanning for classes of a certain type.
 *
 * @param P The owning [MinixPlugin]
 * @param T The target type to scan for.
 */
@Experimental
public abstract class MapperService<P : MinixPlugin, T : Any> : Extension<P>() {
    private val targetType: KClass<T> by lazy { typeArgumentOf(1) }

    protected abstract suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin,
        module: Module
    )

    /** Ensure all references to this plugin are removed. */
    public abstract suspend fun forgetMapped(plugin: MinixPlugin)

    public suspend fun processMapped(
        plugin: MinixPlugin,
        scanner: Scanner
    ) {
        scanner.withSuperclass(targetType).forEach { classInfo ->
            logger.trace { "Registering ${targetType.simpleName} [${plugin.value}:${classInfo.simpleName}]" }
            this.registerMapped(classInfo, plugin)
        }
    }
}
