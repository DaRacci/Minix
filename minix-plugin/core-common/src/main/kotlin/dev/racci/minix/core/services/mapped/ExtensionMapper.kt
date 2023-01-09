package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.handleError
import com.google.common.collect.MultimapBuilder
import dev.racci.minix.api.annotations.Binds
import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.annotations.Required
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.lifecycles.ComplexLifecycle
import dev.racci.minix.api.lifecycles.Lifecycle
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.RecursionUtils
import dev.racci.minix.api.utils.koin
import dev.racci.minix.core.plugin.Minix
import io.github.classgraph.ClassInfo
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.job
import org.koin.core.module.Module
import org.koin.dsl.binds
import kotlin.reflect.KClass
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

@Required
public class ExtensionMapper : MapperService<Minix, Extension<*>>() {
    private val sortedExtensions = MultimapBuilder.treeKeys()
        .treeSetValues(ExtensionSkeleton.Comparator)
        .build<MinixPlugin, KClass<out ExtensionSkeleton<*>>>()

    internal fun registerMapped(
        extension: KClass<out Extension<*>>,
        plugin: MinixPlugin
    ) {
        println("Register extension $extension for plugin ${plugin.value}")
        sortedExtensions.put(plugin, extension)
    }

    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin,
        module: Module
    ) {
        val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<out Extension<*>>>()

        if (plugin.scope.getOrNull<Extension<*>>(kClass) != null) return // Already registered.

        Option.fromNullable(kClass.primaryConstructor)
            .filter { it.parameters.isEmpty() }
            .map { const -> module.single { const.call() } binds Binds.binds(kClass) }
            .handleError { error("Unable to find no-args constructor for `$kClass`.") }

        registerMapped(kClass, plugin)
    }

    override suspend fun forgetMapped(plugin: MinixPlugin) {
        unloadExtensions(plugin)
        sortedExtensions.removeAll(plugin)
    }

    public suspend fun loadExtensions(plugin: MinixPlugin): Unit = with(getExtensions(plugin)) {
        println("Loading sortedExtensions in order: ${joinToString { it.toString() }}")
        forEach { extension -> loadExtension(extension, this) }
    }

    public suspend fun enableExtensions(plugin: MinixPlugin): Unit = with(getExtensions(plugin)) {
        forEach { extension -> enableExtension(extension, this) }
    }

    public suspend fun disableExtensions(plugin: MinixPlugin): Unit = with(getExtensions(plugin)) {
        reversed().forEach { extension -> disableExtension(extension, this) }
    }

    public suspend fun unloadExtensions(plugin: MinixPlugin): Unit = with(getExtensions(plugin)) {
        reversed().forEach { extension -> unloadExtension(koin.get(extension)) }
    }

    public suspend fun loadExtension(
        clazz: KClass<out ExtensionSkeleton<*>>,
        sorted: Set<KClass<out ExtensionSkeleton<*>>>
    ) {
        val extension = koin.get<ExtensionSkeleton<*>>(clazz)
        if (extension.state < ExtensionState.UNLOADED) return

        withState(ExtensionState.LOADING, extension, Lifecycle::handleLoad).fold(
            { println("Loaded extension ${extension.value}.") },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
        extension.plugin
    }

    public suspend fun enableExtension(
        clazz: KClass<out ExtensionSkeleton<*>>,
        sorted: Set<KClass<out ExtensionSkeleton<*>>>
    ) {
        val extension = koin.get<ExtensionSkeleton<*>>(clazz)
        val ordinal = extension.state.ordinal
        if (ordinal !in 0..1 && ordinal !in 4..5) return

        withState(ExtensionState.ENABLING, extension, ComplexLifecycle::handleEnable).fold(
            { logger.trace { "Enabled extension ${extension.value}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
    }

    public suspend fun disableExtension(
        clazz: KClass<out ExtensionSkeleton<*>>,
        sorted: Set<KClass<out ExtensionSkeleton<*>>>
    ) {
        val extension = koin.get<ExtensionSkeleton<*>>(clazz)
        if (extension.state.ordinal !in 0..3) return

        withState(ExtensionState.DISABLING, extension, ComplexLifecycle::handleDisable).fold(
            { logger.trace { "Disabled extension ${extension.value}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
    }

    public suspend fun unloadExtension(extension: ExtensionSkeleton<*>) {
        if (extension.state.ordinal !in 0..5) return

        withState(ExtensionState.UNLOADING, extension, Lifecycle::handleUnload).fold(
            { logger.trace { "Unloaded extension ${extension.value}." } },
            { err -> logger.error(err) { "Extension ${extension.value} threw an error while unloading!" } }
        )

        extension.eventListener.close()

        if (extension::class.hasAnnotation<Required>()) return

        extension.supervisor.coroutineContext.job.castOrThrow<CompletableJob>().complete()
        extension.dispatcher.close()
        sortedExtensions.remove(plugin, extension::class)
        // unloadKoinModules(KoinUtils.getModule(extension))
        // KoinUtils.clearBinds(extension)
    }

    private fun getExtensions(plugin: MinixPlugin): ImmutableSet<KClass<out ExtensionSkeleton<*>>> {
        return sortedExtensions[plugin].toImmutableSet()
    }

    private suspend fun withState(
        state: ExtensionState,
        extension: ExtensionSkeleton<*>,
        func: KSuspendFunction1<ComplexLifecycle, Unit>
    ): Option<Throwable> {
        extension.state = state

        try {
            func.invoke(extension)
        } catch (e: Throwable) {
            logger.error(e) { "Extension ${extension.value} threw an error while ${state.name.lowercase()}!" }
            return Some(e)
        }

        extension.state = ExtensionState.values()[state.ordinal - 1]

        return None
    }

    private suspend fun errorDependents(
        failedExtension: ExtensionSkeleton<*>,
        sorted: Set<KClass<out ExtensionSkeleton<*>>>,
        error: Throwable? = null
    ) {
        // TODO: Handle dependents.
        return
//        val dependents = getRecursiveDependencies(failedExtension, sorted.reversed())
//        dependents.forEach { unloadExtension(it) }
//
//        logger.error(error) {
//            val builder = StringBuilder()
//            builder.append("There was an error while loading / enabling extension ${failedExtension::class.getFullName()}!")
//            builder.append("\nThis is not a fatal error, but it may cause other sortedExtensions to fail to load.")
//            if (dependents.isNotEmpty()) {
//                builder.append("\nThese sortedExtensions will not be loaded:")
//                dependents.forEach { builder.append("\n\t${it.value}") }
//            }
//            builder.toString()
//        }
    }

    private fun getRecursiveDependencies(
        requiredExt: Extension<*>,
        sorted: Set<Extension<*>>
    ): HashSet<Extension<*>> {
        return RecursionUtils.recursiveFinder(
            requiredExt,
            0,
            9,
            { sorted },
            {
                val dependencies = this::class.findAnnotation<Depends>()?.dependencies.orEmpty()
                dependencies.isNotEmpty() && dependencies.none { it in KoinUtils.getBinds(requiredExt) }
            }
        )
//
//        for (extension in allExtensions) {
//            val annotation = extension::class.findAnnotation<MappedExtension>()!!
//            if (annotation.dependencies.isEmpty()) continue
//            if (annotation.dependencies.none { it == bindToKClass(requiredExt) || it == requiredExt::class }) continue
//            if (extension in currentDependents) continue
//
//            currentDependents += extension
//            currentDependents += getRecursiveDependencies(extension, allExtensions, currentDependents)
//        }
//
//        return currentDependents
    }
}
