package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.MinixPlugin
import io.github.classgraph.AnnotationClassRef
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

public interface MapperService<T : MinixPlugin> : ExtensionSkeleton<T> {
    public val superclass: KClass<out Any>
    public val targetAnnotation: KClass<out Annotation>

    public suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    )

    /** Ensure all references to this plugin are removed. */
    public suspend fun forgetMapped(plugin: MinixPlugin)

    public suspend fun processMapped(
        plugin: MinixPlugin,
        scanResult: ScanResult,
        binds: Array<KClass<out Any>>
    ) {
        scanResult.getClassesWithAnnotation(this.targetAnnotation.java).asSequence()
            .associateWith { result(it, binds) }
            .forEach { (classInfo, result) ->
                when (result) {
                    is Some -> logger.error(msg = result.castOrThrow<Some<() -> String>>().value)
                    is None -> {
                        logger.trace { "Registering ${targetAnnotation.simpleName} [${plugin.value}:${classInfo.simpleName}]" }
                        this.registerMapped(classInfo, plugin)
                    }
                }
            }
    }

    private fun result(classInfo: ClassInfo, binds: Array<KClass<out Any>>): Option<() -> String> {
        val annotation = classInfo.getAnnotationInfo(targetAnnotation.java)

        // TODO -> This probably should go now.
        when (val parent = annotation.parameterValues["parent"].value.castOrThrow<AnnotationClassRef>().loadClass().kotlin) {
            in binds -> None
            else -> Some { "Parent class [${parent.simpleName}] is not a valid bind for ${classInfo.simpleName}. - Binds(${binds.joinToString(", ") {it.simpleName!!}})" }
        }.tap { message -> return Some(message) }

        if (!superclass.isSuperclassOf(classInfo.loadClass().kotlin)) {
            return Some { "Class ${classInfo.name} does not extend ${superclass.simpleName}." }
        }

        return None
    }
}
