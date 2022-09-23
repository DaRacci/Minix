package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.collections.clear
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.services.PluginService.Companion.pluginCache
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.RecursionUtils
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.job
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.ext.getFullName
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

@MappedExtension(Minix::class, "Extension Mapper")
class ExtensionService(override val plugin: Minix) : MapperService(
    Extension::class,
    MappedExtension::class
) {
    override fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<Extension<*>>>()
        val constructor = kClass.primaryConstructor!!

        val extension = when (constructor.parameters.size) {
            0 -> constructor.call()
            else -> constructor.call(plugin)
        }

        loadKoinModules(KoinUtils.getModule(extension))
        PluginService.pluginCache[plugin].extensions.add(extension)
    }

    private fun sortInitialized(plugin: MinixPlugin): MutableList<Extension<out MinixPlugin>> {
        val cache = PluginService.pluginCache[plugin]
        val extensions = ArrayDeque(cache.extensions.filter { it.state == ExtensionState.UNLOADED })
        val sortedExtensions = mutableListOf<Extension<out MinixPlugin>>()

        while (extensions.isNotEmpty()) {
            val popped = extensions.removeFirst()

            if (this.checkNoDeps(plugin, popped, sortedExtensions)) continue
            if (this.checkAddedAndMissingDeps(plugin, popped, sortedExtensions, extensions)) continue
            this.checkOrder(plugin, popped, sortedExtensions, extensions)
        }

        return sortedExtensions
    }

    private fun checkNoDeps(
        plugin: MinixPlugin,
        next: Extension<out MinixPlugin>,
        sortedExtensions: MutableList<Extension<out MinixPlugin>>
    ): Boolean {
        val annotation = next::class.findAnnotation<MappedExtension>()!!
        if (annotation.dependencies.isEmpty()) {
            plugin.log.trace { "No dependencies required for ${next.name}, adding to sorted." }
            return sortedExtensions.add(next)
        }

        if (next in sortedExtensions &&
            annotation.dependencies.any { dep -> sortedExtensions.find { it::class == dep } == null }
        ) {
            plugin.log.trace { "Missing dependencies for ${next.name}, Reordering required dependencies." }
            return false
        }

        plugin.log.trace { "All dependencies for ${next.name} are loaded, adding to sorted." }
        return sortedExtensions.add(next)
    }

    private fun checkAddedAndMissingDeps(
        plugin: MinixPlugin,
        next: Extension<out MinixPlugin>,
        sortedExtensions: MutableList<Extension<out MinixPlugin>>,
        extensions: ArrayDeque<Extension<out MinixPlugin>>
    ): Boolean {
        if (next !in sortedExtensions) return false

        plugin.log.trace { "Missing dependency for ${next.name} in ${plugin.name}, Reordering required dependencies." }
        val currentIndex = sortedExtensions.indexOf(next)
        sortedExtensions.removeAt(currentIndex)

        val needed = next::class.findAnnotation<MappedExtension>()!!.dependencies
            .filterNot { sortedExtensions.any { ex -> ex::class == it } }
            .mapNotNull { clazz -> extensions.find { clazz in KoinUtils.getBinds(it) } }

        plugin.log.trace { "Found missing dependencies [${needed.joinToString(", ") { it.name }}] for ${next.name}." }

        sortedExtensions.addAll(currentIndex, needed)
        sortedExtensions.add(currentIndex + needed.size + 1, next)
        return true
    }

    private fun checkOrder(
        plugin: MinixPlugin,
        next: Extension<out MinixPlugin>,
        sortedExtensions: MutableList<Extension<out MinixPlugin>>,
        extensions: MutableList<Extension<out MinixPlugin>>
    ) {
        val index = sortedExtensions.indexOf(next).takeUnless { it == -1 } ?: (sortedExtensions.lastIndex + 1)
        if (next in sortedExtensions) {
            sortedExtensions.removeAt(index)
        }

        val toAdd = mutableListOf<Extension<out MinixPlugin>>()
        for (dependency in next::class.findAnnotation<MappedExtension>()!!.dependencies) {
            if (sortedExtensions.find { it::class == dependency } != null) continue // Already added

            extensions.find { it::class == dependency }?.let {
                toAdd += it
                plugin.log.trace { "Adding required dependency ${it.name} before ${next.name}" }
            }
        }
        toAdd += next
        sortedExtensions.addAll(index, toAdd)
    }

    suspend fun loadExtensions(plugin: MinixPlugin) {
        val sorted = sortInitialized(plugin)
        for (extension in sorted) { loadExtension(extension, sorted) }
    }

    suspend fun enableExtensions(plugin: MinixPlugin) {
        val extensions = pluginCache[plugin].extensions
        extensions.forEach { enableExtension(it, extensions) }
    }

    suspend fun disableExtensions(extensions: List<Extension<*>>) {
        val reversed = extensions.reversed()
        reversed.forEach { disableExtension(it, reversed) }
    }

    suspend fun unloadExtensions(extensions: MutableList<Extension<*>>) = extensions.asReversed().clear(::unloadExtension)

    suspend fun loadExtension(
        extension: Extension<*>,
        sorted: MutableList<Extension<*>>
    ) {
        withState(ExtensionState.LOADING, extension) {
            extension.handleLoad()
        }.fold(
            { logger.trace { "Loaded extension ${extension.name}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
    }

    suspend fun enableExtension(
        extension: Extension<*>,
        sorted: MutableList<Extension<*>>
    ) {
        if (extension.state.ordinal > 5) return

        withState(ExtensionState.ENABLING, extension) {
            extension.handleEnable()
        }.fold(
            { logger.trace { "Enabled extension ${extension.name}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
    }

    suspend fun disableExtension(
        extension: Extension<*>,
        extensionList: List<Extension<*>>
    ) {
        if (extension.state.ordinal > 4) return

        withState(ExtensionState.DISABLING, extension) {
            extension.handleDisable()
        }.fold(
            { logger.trace { "Disabled extension ${extension.name}." } },
            { err -> errorDependents(extension, extensionList, err); unloadExtension(extension) }
        )
    }

    private suspend fun unloadExtension(extension: Extension<*>) {
        if (extension.state.ordinal > 5) return

        logger.trace { "Unloading extension ${extension.name}." }

        withState(ExtensionState.UNLOADING, extension) {
            extension.handleUnload()
        }.fold(
            { logger.trace { "Unloaded extension ${extension.name}." } },
            { err -> logger.error(err) { "Extension ${extension.name} threw an error while unloading!" } }
        )

        extension.eventListener.unregisterListener()
        extension.supervisor.coroutineContext.job.castOrThrow<CompletableJob>().complete()
        extension.dispatcher.close()
        pluginCache[extension.plugin].extensions.remove(extension)
        unloadKoinModules(KoinUtils.getModule(extension))
    }

    private suspend fun withState(
        state: ExtensionState,
        extension: Extension<*>,
        block: suspend () -> Unit
    ): Option<Throwable> {
        extension.setState(state)

        try {
            block()
        } catch (e: Throwable) {
            logger.error(e) { "Extension ${extension.name} threw an error while ${state.name.lowercase()}!" }
            return Some(e)
        }

        extension.setState(ExtensionState.values()[state.ordinal - 1])
        return None
    }

    private suspend fun errorDependents(
        failedExtension: Extension<*>,
        extensions: List<Extension<*>>,
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
                dependents.forEach { builder.append("\n\t${it.name}") }
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
                val annotation = this::class.findAnnotation<MappedExtension>()!!
                annotation.dependencies.isNotEmpty() && annotation.dependencies.none { it in KoinUtils.getBinds(requiredExt) }
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
