package dev.racci.minix.core.services.mapped

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.toOption
import com.google.common.collect.TreeMultimap
import dev.racci.minix.api.annotations.DoNotUnload
import dev.racci.minix.api.annotations.MappedExtension
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

@DoNotUnload
@MappedExtension
public class ExtensionMapper(
    override val plugin: Minix
) : MapperService<Minix>, Extension<Minix>() {
    override val superclass: KClass<*> = Extension::class
    override val targetAnnotation: KClass<out Annotation> = MappedExtension::class

    // Create a function that orders extensions by their dependencies
//    private fun orderExtensions(g: MutableGraph<Extension<*>>): ImmutableList<Extension<*>> {
//        val extensions = g.nodes().toList()
//        // Create a graph of the extensions
//        val graph: MutableGraph<Extension<*>> = GraphBuilder.directed().build()
//
//        // Add all the extensions to the graph
//        extensions.forEach { graph.addNode(it) }
//
//        // Add all the dependencies to the graph
//        extensions.forEach { extension ->
//            extension::class.findAnnotation<MappedExtension>()!!.dependencies.forEach { dependencyClazz ->
//                val dependency = getKoin().get<Extension<*>>(dependencyClazz)
//                graph.putEdge(extension, dependency)
//            }
//        }
// //
// //        graph = g
//
//        // Create a list of extensions that are not dependent on any other extensions
//        val independentExtensions = extensions.filter { extension ->
//            graph.inDegree(extension) == 0
//        }
//
//        // Create a list of extensions that are dependent on other extensions
//        val dependentExtensions = extensions.filter { extension ->
//            graph.inDegree(extension) != 0
//        }
//
//        // Create a list of extensions that are dependent on other extensions
//        val orderedExtensions = mutableListOf<Extension<*>>()
//
//        // Add all the independent extensions to the ordered list
//        orderedExtensions.addAll(independentExtensions)
//
//        // Add all the dependent extensions to the ordered list
//        dependentExtensions.forEach { extension ->
//            // Get the index of the extension in the ordered list
//            val index = orderedExtensions.indexOfFirst { orderedExtension ->
//                // Check if the extension is dependent on the ordered extension
//                graph.hasEdgeConnecting(extension, orderedExtension)
//            }
//
//            if (index == -1) {
//                orderedExtensions.add(extension)
//                return@forEach
//            }
//
//            // Add the extension to the ordered list
//            orderedExtensions.add(index, extension)
//        }
//
//        // Reverse the list so that the extensions are in the correct order
//        orderedExtensions.reverse()
//
//        return orderedExtensions.toImmutableList()
//    }
//
//    private val topoSorted = ConcurrentHashMap<MinixPlugin, ImmutableList<Extension<*>>>() // Topologically sorted extensions
//
//    private val extensionGraph = GraphBuilder.directed().build<Extension<*>>()
//    private val neededEdges = ConcurrentHashMap<Extension<*>, MutableList<KClass<*>>>()

    private val extensions = TreeMultimap.create(PlatformPlugin::compareTo, ExtensionComparator)

    internal fun registerMapped(
        extension: Extension<*>,
        plugin: MinixPlugin
    ) {
//        val graph = extensionGraph
//        graph.addNode(extension)
//
//        for ((node, needed) in neededEdges.entries) {
//            if (!needed.contains(extension::class)) continue
//            graph.putEdge(extension, node)
//            needed.remove(extension::class)
//
//            if (needed.isNotEmpty()) continue
//            neededEdges.remove(node)
//        }
//
//        for (dependency in extension::class.findAnnotation<MappedExtension>()!!.dependencies) {
//            val dependencyExtension = extensionGraph.nodes().find { dependency in KoinUtils.getBinds(it) }
//            if (dependencyExtension != null) {
//                graph.putEdge(extension, dependencyExtension)
//            } else {
//                neededEdges.computeIfAbsent(extension) { mutableListOf() }.add(dependency)
//            }
//        }
//
//        topoSorted.remove(plugin) // Ensures resorting of extensions
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

        if (extension::class.hasAnnotation<DoNotUnload>()) return

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
