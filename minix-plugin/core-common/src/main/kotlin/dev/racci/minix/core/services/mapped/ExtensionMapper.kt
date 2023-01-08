package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.toOption
import com.google.common.collect.TreeMultimap
import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.annotations.Required
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PlatformPlugin
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.RecursionUtils
import dev.racci.minix.core.comparator.ExtensionComparator
import dev.racci.minix.core.plugin.Minix
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.job
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.ext.getFullName
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

@Required
public class ExtensionMapper : MapperService<Minix, Extension<*>>() {
    private val extensions = TreeMultimap.create(PlatformPlugin::compareTo, ExtensionComparator)

    internal fun registerMapped(
        extension: Extension<*>,
        plugin: MinixPlugin
    ) {
        extensions.put(plugin, extension)
    }

    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<Extension<*>>>()

        val extension = getKoin().getOrNull<Extension<*>>(kClass::class).toOption()
            .getOrElse {
                val constructor = kClass.primaryConstructor ?: error("No primary constructor found for extension ${kClass.qualifiedName}")
                val hasPluginParam = constructor.findParameterByName("plugin")?.type?.isSubtypeOf(MinixPlugin::class.starProjectedType) ?: false
                val res = if (hasPluginParam) constructor.call(getKoin().get<MinixPlugin>(plugin::class)) else constructor.call()

                loadKoinModules(KoinUtils.getModule(res))
                res.scope.linkTo(plugin.scope)
                res
            }

        registerMapped(extension, plugin)
    }

    override suspend fun forgetMapped(plugin: MinixPlugin) {
        unloadExtensions(plugin)

        // neededEdges.keys.removeAll { it.plugin == plugin }
        // topoSorted.remove(plugin)
        extensions.removeAll(plugin)
    }

    public suspend fun loadExtensions(plugin: MinixPlugin) {
        extensions[plugin]
            .also { plugin.logger.debug { "Loading extensions in order: ${it.joinToString { it.value }}" } }
            .forEach { extension -> this.loadExtension(extension, extensions[plugin]) }

//        topoSorted.computeIfAbsent(plugin) { orderExtensions(extensionGraph ?: error("${plugin.value} doesn't have a graph!")) }
//            .filter { it.plugin === plugin }
//            .also { plugin.logger.debug { "Loading extensions in order: ${it.joinToString { it.value }}" } }
//            .forEach { extension -> this.loadExtension(extension, topoSorted[plugin]!!) }
    }

    public suspend fun enableExtensions(plugin: MinixPlugin) {
        getExtensions(plugin).forEach { extension -> this.enableExtension(extension, extensions[plugin] /*topoSorted[plugin]!!*/) }
    }

    public suspend fun disableExtensions(plugin: MinixPlugin) {
        getExtensions(plugin).reversed().forEach { extension -> this.disableExtension(extension, extensions[plugin] /*topoSorted[plugin]!!*/) }
    }

    public suspend fun unloadExtensions(plugin: MinixPlugin) {
        getExtensions(plugin).reversed().forEach { extension -> this.unloadExtension(extension) }
    }

    public suspend fun loadExtension(
        extension: Extension<*>,
        sorted: Collection<Extension<*>>
    ) {
        if (extension.state < ExtensionState.UNLOADED) return

        withState(ExtensionState.LOADING, extension) {
            extension.handleLoad()
        }.fold(
            { logger.trace { "Loaded extension ${extension.value}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
        extension.plugin
    }

    public suspend fun enableExtension(
        extension: Extension<*>,
        sorted: Collection<Extension<*>>
    ) {
        val ordinal = extension.state.ordinal
        if (ordinal !in 0..1 && ordinal !in 4..5) return

        withState(ExtensionState.ENABLING, extension) {
            extension.handleEnable()
        }.fold(
            { logger.trace { "Enabled extension ${extension.value}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
    }

    public suspend fun disableExtension(
        extension: Extension<*>,
        extensions: Collection<Extension<*>>
    ) {
        if (extension.state.ordinal !in 0..3) return

        withState(ExtensionState.DISABLING, extension) {
            extension.handleDisable()
        }.fold(
            { logger.trace { "Disabled extension ${extension.value}." } },
            { err -> errorDependents(extension, extensions, err); unloadExtension(extension) }
        )
    }

    public suspend fun unloadExtension(extension: Extension<*>) {
        if (extension.state.ordinal !in 0..5) return

        withState(ExtensionState.UNLOADING, extension) {
            extension.handleUnload()
        }.fold(
            { logger.trace { "Unloaded extension ${extension.value}." } },
            { err -> logger.error(err) { "Extension ${extension.value} threw an error while unloading!" } }
        )

        extension.eventListener.close()

        if (extension::class.hasAnnotation<Required>()) return

        extension.supervisor.coroutineContext.job.castOrThrow<CompletableJob>().complete()
        extension.dispatcher.close()
        extensions.remove(plugin, extension)
        // runCatching { extensionGraph.removeNode(extension) }.onFailure { logger.error { "Failed to remove extension ${extension.name} from graph!" } }
        unloadKoinModules(KoinUtils.getModule(extension))
        KoinUtils.clearBinds(extension)
    }

    private fun getExtensions(plugin: MinixPlugin): Set<Extension<*>> {
        return extensions[plugin]

        // topoSorted[plugin]!!
        //     .filter { it.plugin === plugin }
        //     .also { logger.debug { "Order: ${it.joinToString(", ", transform = Extension<*>::value)}" } }
    }

    private suspend fun withState(
        state: ExtensionState,
        extension: Extension<*>,
        block: suspend () -> Unit
    ): Option<Throwable> {
        extension.state = state

        try {
            block()
        } catch (e: Throwable) {
            logger.error(e) { "Extension ${extension.value} threw an error while ${state.name.lowercase()}!" }
            return Some(e)
        }

        extension.state = ExtensionState.values()[state.ordinal - 1]

        return None
    }

    private suspend fun errorDependents(
        failedExtension: Extension<*>,
        extensions: Collection<Extension<*>>,
        error: Throwable? = null
    ) {
        val dependents = getRecursiveDependencies(failedExtension, extensions.reversed())
        dependents.forEach { unloadExtension(it) }

        logger.error(error) {
            val builder = StringBuilder()
            builder.append("There was an error while loading / enabling extension ${failedExtension::class.getFullName()}!")
            builder.append("\nThis is not a fatal error, but it may cause other extensions to fail to load.")
            if (dependents.isNotEmpty()) {
                builder.append("\nThese extensions will not be loaded:")
                dependents.forEach { builder.append("\n\t${it.value}") }
            }
            builder.toString()
        }
    }

    private fun getRecursiveDependencies(
        requiredExt: Extension<*>,
        allExtensions: List<Extension<*>>
    ): HashSet<Extension<*>> {
        return RecursionUtils.recursiveFinder(
            requiredExt,
            0,
            9,
            { allExtensions },
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
