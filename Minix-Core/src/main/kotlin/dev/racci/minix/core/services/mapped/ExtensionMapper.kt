package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.toOption
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.RecursionUtils
import io.github.classgraph.ClassInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.job
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.ext.getFullName
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

@Suppress("UnstableApiUsage")
@MappedExtension(Minix::class, "Extension Mapper")
class ExtensionMapper(override val plugin: Minix) : MapperService(
    Extension::class,
    MappedExtension::class
) {
    // Create a function that orders extensions by their dependencies
    private fun orderExtensions(g: MutableGraph<Extension<*>>): ImmutableList<Extension<*>> {
        val extensions = g.nodes().toList()
        // Create a graph of the extensions
        val graph: MutableGraph<Extension<*>> = GraphBuilder.directed().build()

        // Add all the extensions to the graph
        extensions.forEach { graph.addNode(it) }

        // Add all the dependencies to the graph
        extensions.forEach { extension ->
            extension::class.findAnnotation<MappedExtension>()!!.dependencies.forEach { dependencyClazz ->
                val dependency = getKoin().get<Extension<*>>(dependencyClazz)
                graph.putEdge(extension, dependency)
            }
        }
//
//        graph = g

        // Create a list of extensions that are not dependent on any other extensions
        val independentExtensions = extensions.filter { extension ->
            graph.inDegree(extension) == 0
        }

        // Create a list of extensions that are dependent on other extensions
        val dependentExtensions = extensions.filter { extension ->
            graph.inDegree(extension) != 0
        }

        // Create a list of extensions that are dependent on other extensions
        val orderedExtensions = mutableListOf<Extension<*>>()

        // Add all the independent extensions to the ordered list
        orderedExtensions.addAll(independentExtensions)

        // Add all the dependent extensions to the ordered list
        dependentExtensions.forEach { extension ->
            // Get the index of the extension in the ordered list
            val index = orderedExtensions.indexOfFirst { orderedExtension ->
                // Check if the extension is dependent on the ordered extension
                graph.hasEdgeConnecting(extension, orderedExtension)
            }

            if (index == -1) {
                orderedExtensions.add(extension)
                return@forEach
            }

            // Add the extension to the ordered list
            orderedExtensions.add(index, extension)
        }

        // Reverse the list so that the extensions are in the correct order
        orderedExtensions.reverse()

        return orderedExtensions.toImmutableList()
    }

    private val topoSorted = ConcurrentHashMap<KClass<*>, ImmutableList<Extension<*>>>() // Topologically sorted extensions

    private val extensionGraph = GraphBuilder.directed().build<Extension<*>>()
    private val neededEdges = ConcurrentHashMap<Extension<*>, MutableList<KClass<*>>>()

    internal fun registerMapped(
        extension: Extension<*>,
        plugin: MinixPlugin
    ) {
        val graph = extensionGraph
        graph.addNode(extension)

        for ((node, needed) in neededEdges.entries) {
            if (!needed.contains(extension::class)) continue
            graph.putEdge(extension, node)
            needed.remove(extension::class)

            if (needed.isNotEmpty()) continue
            neededEdges.remove(node)
        }

        for (dependency in extension::class.findAnnotation<MappedExtension>()!!.dependencies) {
            val dependencyExtension = extensionGraph.nodes().find { it::class == dependency }
            if (dependencyExtension != null) {
                graph.putEdge(extension, dependencyExtension)
            } else {
                neededEdges.computeIfAbsent(extension) { mutableListOf() }.add(dependency)
            }
        }

        topoSorted.remove(plugin::class)
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
                res
            }

        registerMapped(extension, plugin)
    }

    override suspend fun forgetMapped(
        plugin: MinixPlugin,
        cache: PluginData<*>
    ) {
        extensionGraph.nodes()
            .filter { it.plugin == plugin }
            .onEach(extensionGraph::removeNode)
            .onEach { unloadKoinModules(KoinUtils.getModule(it)) }
            .forEach(KoinUtils::clearBinds)

        neededEdges.keys.removeAll { it.plugin == plugin }
        topoSorted.remove(plugin::class)
    }

    suspend fun loadExtensions(plugin: MinixPlugin) {
        topoSorted.computeIfAbsent(plugin::class) { orderExtensions(extensionGraph ?: error("${plugin.name} doesn't have a graph!")) }
            .filter { it.plugin === plugin }
            .also { plugin.log.debug { "Loading extensions in order: ${it.joinToString { it.name }}" } }
            .forEach { extension -> this.loadExtension(extension, topoSorted[plugin::class]!!) }
    }

    suspend fun enableExtensions(plugin: MinixPlugin) {
        topoSorted[plugin::class]!!.filter { it.plugin === plugin }.forEach { extension -> this.enableExtension(extension, topoSorted[plugin::class]!!) }
    }

    suspend fun disableExtensions(plugin: MinixPlugin) {
        topoSorted[plugin::class]!!.filter { it.plugin === plugin }.reversed().forEach { extension -> this.disableExtension(extension, topoSorted[plugin::class]!!) }
    }

    suspend fun unloadExtensions(plugin: MinixPlugin) {
        topoSorted[plugin::class]!!.filter { it.plugin === plugin }.reversed().forEach { extension -> this.unloadExtension(extension) }
    }

    suspend fun loadExtension(
        extension: Extension<*>,
        sorted: List<Extension<*>>
    ) {
        if (extension.loaded) return logger.warn { "Extension ${extension.name} in state, [${extension.state}], cannot load.!" }

        withState(ExtensionState.LOADING, extension) {
            extension.handleLoad()
        }.fold(
            { logger.trace { "Loaded extension ${extension.name}." } },
            { err -> errorDependents(extension, sorted, err); unloadExtension(extension) }
        )
        extension.plugin
    }

    suspend fun enableExtension(
        extension: Extension<*>,
        sorted: List<Extension<*>>
    ) {
        val ordinal = extension.state.ordinal
        if (ordinal !in 0..1 && ordinal !in 4..5) return logger.warn { "Extension ${extension.name} in state [${extension.state}], cannot enable." }

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
        if (extension.state.ordinal !in 0..3) return logger.warn { "Extension ${extension.name} in state [${extension.state}], cannot disable." }

        withState(ExtensionState.DISABLING, extension) {
            extension.handleDisable()
        }.fold(
            { logger.trace { "Disabled extension ${extension.name}." } },
            { err -> errorDependents(extension, extensionList, err); unloadExtension(extension) }
        )
    }

    suspend fun unloadExtension(extension: Extension<*>) {
        if (extension.state.ordinal !in 0..5) return logger.warn { "Extension ${extension.name} in state [${extension.state}], cannot unload." }

        withState(ExtensionState.UNLOADING, extension) {
            extension.handleUnload()
        }.fold(
            { logger.trace { "Unloaded extension ${extension.name}." } },
            { err -> logger.error(err) { "Extension ${extension.name} threw an error while unloading!" } }
        )

        extension.eventListener.unregisterListener()
        extension.supervisor.coroutineContext.job.castOrThrow<CompletableJob>().complete()
        extension.dispatcher.close()
        runCatching { extensionGraph.removeNode(extension) }.onFailure { logger.error { "Failed to remove extension ${extension.name} from graph!" } }
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
