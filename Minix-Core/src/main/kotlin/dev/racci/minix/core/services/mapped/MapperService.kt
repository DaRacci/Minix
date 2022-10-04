package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import io.github.classgraph.AnnotationClassRef
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

abstract class MapperService internal constructor(
    private val superclass: KClass<out Any>,
    private val targetAnnotation: KClass<out Annotation>
) : Extension<Minix>() {
    override val plugin: Minix by inject()

    abstract suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    )

    /** Ensure all references to this plugin are removed. */
    abstract suspend fun forgetMapped(
        plugin: MinixPlugin,
        cache: PluginData<*>
    )

    suspend fun processMapped(
        plugin: MinixPlugin,
        scanResult: ScanResult,
        binds: Array<KClass<out Any>>
    ) {
        scanResult.getClassesWithAnnotation(this.targetAnnotation.java).asSequence()
            .map { MappedResult(it, binds) }
            .forEach { result ->
                when (result.message) {
                    is Some -> logger.error(msg = result.message.castOrThrow<Some<() -> String>>().value)
                    is None -> {
                        logger.trace { "Registering ${targetAnnotation.simpleName} [${plugin.name}:${result.classInfo.simpleName}]" }
                        this.registerMapped(result.classInfo, plugin)
                    }
                }
            }
    }

    private inner class MappedResult(
        val classInfo: ClassInfo,
        val binds: Array<KClass<out Any>>
    ) {
        var message: Option<() -> String> = None

        init {
            runBlocking {
                checkAnnotation()
                checkSuperclass()
            }
        }

        private suspend fun checkAnnotation() {
            when (message) {
                is Some -> None
                is None -> {
                    val annotation = classInfo.getAnnotationInfo(targetAnnotation.java)

                    when (val parent = annotation.parameterValues["parent"].value.castOrThrow<AnnotationClassRef>().loadClass().kotlin) {
                        in binds -> None
                        else -> Some { "Parent class [${parent.simpleName}] is not a valid bind for ${classInfo.simpleName}. - Binds(${binds.joinToString(", ") {it.simpleName!!}})" }
                    }
                }
            }
        }

        private fun checkSuperclass() {
            when (message) {
                is Some -> None
                is None -> when (superclass.isSuperclassOf(classInfo.loadClass().kotlin)) {
                    true -> None
                    false -> message = Some { "Class ${classInfo.name} does not extend ${superclass.simpleName}." }
                }
            }
        }
    }
}
