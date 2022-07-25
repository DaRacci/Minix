@file:OptIn(MinixInternal::class)

package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.plugin.SusPlugin
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.ifNotEmpty
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import dev.racci.minix.api.utils.kotlin.invokeIfOverrides
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import dev.racci.minix.core.coroutine.service.CoroutineSessionImpl
import io.github.classgraph.AnnotationClassRef
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.ext.getFullName
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.time.Duration.Companion.seconds

class PluginServiceImpl(val minix: Minix) : PluginService, KoinComponent {
    private val dataService by inject<DataService>()

    override val loadedPlugins by lazy { mutableMapOf<KClass<out MinixPlugin>, MinixPlugin>() }
    override val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>> = Caffeine.newBuilder().build(::PluginData)
    override val coroutineSession: LoadingCache<MinixPlugin, CoroutineSession> = Caffeine.newBuilder().build { plugin ->
        if (!plugin.isEnabled) {
            plugin.log.throwing(
                RuntimeException(
                    """
                    Plugin ${plugin.name} attempted to start a new coroutine session while being disabled.
                    Dispatchers such as plugin.minecraftDispatcher and plugin.asyncDispatcher are already
                    disposed of at this point and cannot be used.
                    """.trimIndent()
                )
            )
        }
        CoroutineSessionImpl(plugin)
    }

    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin].unsafeCast()

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            if (!plugin.annotation?.extensions.isNullOrEmpty()) {
                for (clazz in plugin.annotation!!.extensions) {
                    if (!Extension::class.isSuperclassOf(clazz)) {
                        plugin.log.error { "$clazz isn't an extension.. Skipping." }
                        continue
                    }

                    pluginCache[plugin].extensions += { plugin: MinixPlugin ->
                        try {
                            val const = clazz.constructors.firstOrNull {
                                it.parameters.size == 1 &&
                                    it.parameters[0].name == "plugin" &&
                                    !it.parameters[0].type.isMarkedNullable &&
                                    it.parameters[0].type.isSubtypeOf(MinixPlugin::class.starProjectedType)
                            } ?: throw IllegalArgumentException("Extension class $clazz does not have a constructor with one parameter of type MinixPlugin!")
                            const.call(plugin) as Extension<*>
                        } catch (e: Exception) {
                            plugin.log.error(e) { "Failed to create extension ${clazz.simpleName} for ${plugin.name}" }
                            throw e
                        }
                    }
                }
            }

            loadModule { single { plugin } bind (plugin.bindToKClass ?: plugin::class).unsafeCast() }
            plugin.invokeIfOverrides(SusPlugin::handleLoad.name) { plugin.handleLoad() }
            plugin.loadReflection()
            pluginCache[plugin].extensions.ifNotEmpty { plugin.loadInOrder() }
            plugin.invokeIfOverrides(SusPlugin::handleAfterLoad.name) {
                plugin.log.debug { "Running handleAfterLoad for ${plugin.name}" }
                plugin.handleAfterLoad()
            }
        }
    }

    override fun startPlugin(plugin: MinixPlugin) {
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            val cache = pluginCache[plugin]

            plugin.invokeIfOverrides(SusPlugin::handleEnable.name) {
                plugin.log.debug { "Running handleEnable for ${plugin.name}" }
                plugin.handleEnable()
            }

            if (cache.extensions.isNotEmpty() || cache.loadedExtensions.isNotEmpty()) {
                plugin.startInOrder()
            }

            plugin.bStatsId.invokeIfNotNull {
                plugin.log.debug { "Registering bStats for ${plugin.name}" }
                cache.metrics = Metrics(plugin, it)
            }

            plugin.invokeIfOverrides(SusPlugin::handleAfterEnable.name) {
                plugin.log.debug { "Running handleAfterEnable for ${plugin.name}" }
                plugin.handleAfterEnable()
            }

            loadedPlugins += plugin::class to plugin
        }
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                plugin.log.debug { "Cancelling ${it.size} tasks for ${plugin.name}" }
                it.forEach { id -> CoroutineScheduler.cancelTask(id) }
            }

            val cache = pluginCache.getIfPresent(plugin)

            cache?.loadedExtensions?.takeIf(MutableList<*>::isNotEmpty)?.let { ex ->
                plugin.log.debug { "Unloading ${ex.size} extensions for ${plugin.name}" }
                plugin.shutdownInOrder()
            }

            plugin.invokeIfOverrides(SusPlugin::handleDisable.name) {
                plugin.log.debug { "Running handleDisable for ${plugin.name}" }
                plugin.handleDisable()
            }

            cache?.configurations?.takeIf(MutableList<*>::isNotEmpty)?.let { configs ->
                plugin.log.debug { "Unloading ${configs.size} configurations for ${plugin.name}" }
                dataService.configurations.invalidateAll(configs)
            }

            plugin.log.debug { "Disabling the coroutine session for ${plugin.name}" }
            coroutineService.disable(plugin)
            loadedPlugins -= plugin::class
        }
    }

    override fun fromClassloader(classLoader: ClassLoader): MinixPlugin? {
        for (plugin in loadedPlugins.values) {
            if (pluginCache[plugin].loader != classLoader) continue
            return plugin
        }
        return null
    }

    private fun MinixPlugin.loadReflection() {
        val classGraph = ClassGraph()
            .acceptPackages("dev.racci")
            .addClassLoader(this::class.java.classLoader)
            .addClassLoader(
                JavaPlugin::class.declaredMemberProperties.first {
                    it.name == "classLoader"
                }.also { it.isAccessible = true }.getter.call(this) as ClassLoader
            )
            .enableClassInfo()
            .enableAnnotationInfo()
            .enableMethodInfo()
            .scan()

        classGraph.getClassesWithAnnotation(MappedExtension::class.java)
            .filter { clazz ->
                matchingAnnotation<MappedExtension>(this, clazz, "extension") &&
                    if (!clazz.extendsSuperclass(Extension::class.java)) {
                        log.warn { "${clazz.name} is annotated with MappedExtension but isn't an extension!." }
                        false
                    } else true
            }
            .forEach { clazz ->
                pluginCache[this].extensions += { plugin: MinixPlugin ->
                    try {
                        clazz.loadClass().kotlin.primaryConstructor!!.call(plugin) as Extension<*>
                    } catch (e: Exception) {
                        log.error(e) { "Failed to create extension ${clazz.simpleName} for ${plugin.name}" }
                        throw e
                    }
                }
            }

        classGraph.getClassesWithAnnotation(MappedConfig::class.java)
            .filter { matchingAnnotation<MappedConfig>(this, it, "configuration") }
            .forEach {
                log.debug { "Found MappedConfig [${it.simpleName}] from ${this.name}" }
                try {
                    get<DataService>().configurations[it.loadClass().kotlin] // Call the cache so we load can have it loaded.
                } catch (ignored: NoBeanDefFoundException) {} // This is so i can auto-magic load the data service.
            }
    }

    private inline fun <reified T : Annotation> matchingAnnotation(
        plugin: MinixPlugin,
        clazz: ClassInfo,
        type: String
    ): Boolean {
        val annotation = clazz.getAnnotationInfo(T::class.java)
        val classRef = annotation.parameterValues["parent"].value.safeCast<AnnotationClassRef>()?.loadClass()?.kotlin
        return if (annotation.parameterValues["parent"].value is AnnotationClassRef &&
            classRef == plugin::class || classRef == plugin.bindToKClass
        ) {
            plugin.log.debug { "Found $type ${clazz.name} from ${plugin.name}" }
            true
        } else {
            plugin.log.debug { "Skipping $type ${clazz.name} because it isn't loaded by ${plugin.name}" }
            false
        }
    }

    private inline fun <reified P : MinixPlugin> P.getSortedExtensions(): MutableList<Extension<P>> {
        val extensions = pluginCache[this].let { cache ->
            cache.extensions
                .map { it.invoke(this) }
                .filterIsInstance<Extension<P>>()
                .toMutableList().also { it.addAll(cache.loadedExtensions.toMutableList().unsafeCast()) }
        }
        val sortedExtensions = mutableListOf<Extension<P>>()
        while (extensions.isNotEmpty()) {
            val next = extensions.first()
            extensions.remove(next)

            if (checkNoDeps(next, sortedExtensions)) continue
            if (checkAddedAndMissingDeps(next, sortedExtensions, extensions)) continue
            checkOrder(next, sortedExtensions, extensions)
        }
        pluginCache[this].loadedExtensions.clear()
        return sortedExtensions
    }

    private inline fun <reified P : MinixPlugin> MinixPlugin.checkOrder(
        next: Extension<P>,
        sortedExtensions: MutableList<Extension<P>>,
        extensions: MutableList<Extension<P>>
    ) {
        val index = sortedExtensions.indexOf(next).takeUnless { it == -1 } ?: (sortedExtensions.lastIndex + 1)
        if (next in sortedExtensions) {
            sortedExtensions.removeAt(index)
        }

        val toAdd = mutableListOf<Extension<P>>()
        for (dependency in next.dependencies) {
            if (sortedExtensions.find { it::class == dependency } != null) continue // Already added

            extensions.find { it::class == dependency }?.let {
                toAdd += it
                log.debug { "Adding required dependency ${it.name} before ${next.name}" }
            }
        }
        toAdd += next
        sortedExtensions.addAll(index, toAdd)
    }

    private inline fun <reified P : MinixPlugin> MinixPlugin.checkAddedAndMissingDeps(
        next: Extension<P>,
        sortedExtensions: MutableList<Extension<P>>,
        extensions: MutableList<Extension<P>>
    ): Boolean {
        if (next !in sortedExtensions) return false

        log.debug { "Missing dependency for ${next.name} in ${this.name}, Reordering required dependencies." }
        val currentIndex = sortedExtensions.indexOf(next)
        sortedExtensions.removeAt(currentIndex)

        val needed = next.dependencies
            .filterNot { sortedExtensions.any { ex -> ex::class == it } }
            .mapNotNull { clazz -> extensions.find { it::class == clazz || it.bindToKClass == clazz } }

        sortedExtensions.addAll(currentIndex, needed)
        sortedExtensions.add(currentIndex + needed.size + 1, next)
        return true
    }

    private inline fun <reified P : MinixPlugin> MinixPlugin.checkNoDeps(
        next: Extension<P>,
        sortedExtensions: MutableList<Extension<P>>
    ): Boolean {
        if (next in sortedExtensions ||
            next.dependencies.isNotEmpty() &&
            next.dependencies.all { dep -> sortedExtensions.find { it::class == dep } == null }
        ) return false

        log.debug { "All dependencies for ${next.name} are loaded, adding to sorted" }
        return sortedExtensions.add(next)
    }

    // TODO: Refactor the dependency system to be more efficient and less hacky maybe using a shared flow
    private suspend fun MinixPlugin.loadInOrder() {
        val sorted = getSortedExtensions()
        sorted.forEach { ex -> ex.start(ExtensionState.LOADING, sorted) }
        pluginCache[this].extensions.clear()
    }

    private suspend fun MinixPlugin.startInOrder() {
        val sorted = getSortedExtensions()
        sorted.filter { it.checkFailed() }.forEach { ex -> ex.start(ExtensionState.ENABLING, sorted) }
        pluginCache[this].extensions.clear()
    }

    private fun Extension<MinixPlugin>.checkFailed(): Boolean {
        when (this.state) {
            ExtensionState.FAILED_DEPENDENCIES -> log.warn { "Extension $name had one or more dependencies fail to load, Skipping." }
            ExtensionState.FAILED_LOADING -> log.warn { "Extension $name had previously failed to load, Skipping." }
            ExtensionState.FAILED_UNLOADING -> log.warn { "Extension $name had previously failed to unload, Skipping." }
            ExtensionState.LOADING, ExtensionState.ENABLING, ExtensionState.UNLOADING -> log.warn { "Extension $name is still ${state.name.lowercase()}, Skipping." }
            else -> return true
        }
        return false
    }

    private suspend fun Extension<MinixPlugin>.start(
        state: ExtensionState,
        sorted: MutableList<Extension<MinixPlugin>>
    ) {
        if (this.state == ExtensionState.FAILED_DEPENDENCIES) {
            return // Don't start failed dependencies, let them disappear into the void of the garbage collector
        }

        val module = getModule()
        val (handle, invoke, log) = getTriple(state)
        setState(state)

        try {
            module?.let(::loadKoinModules)
            withTimeout(5.seconds) {
                this@start.invokeIfOverrides(handle) {
                    log()
                    invoke()
                }
            }
        } catch (e: Throwable) {
            if (e is TimeoutCancellationException) { this.log.warn { "Extension $name took too longer than 5 seconds to load!" } }

            when (state) {
                ExtensionState.LOADING -> setState(ExtensionState.FAILED_LOADING)
                ExtensionState.ENABLING -> setState(ExtensionState.FAILED_ENABLING)
                else -> {}
            }
            errorDependents(sorted, e)
            module?.let(::unloadKoinModules)
        }

        when (state) {
            ExtensionState.LOADING -> setState(ExtensionState.LOADED)
            ExtensionState.ENABLING -> setState(ExtensionState.ENABLED)
            else -> {}
        }
        bound = true
        pluginCache[this@start.plugin].loadedExtensions += this
    }

    private fun Extension<MinixPlugin>.getTriple(state: ExtensionState) = when (state) {
        ExtensionState.LOADING -> {
            Triple(
                Extension<MinixPlugin>::handleLoad.name,
                suspend { this.handleLoad() }
            ) { log.info { "Loading extension $name" } }
        }
        ExtensionState.ENABLING -> {
            Triple(
                Extension<MinixPlugin>::handleEnable.name,
                suspend { this.handleEnable() }
            ) { log.info { "Enabling extension $name" } }
        }
        else -> throw IllegalArgumentException("Cannot get triple for state $state")
    }

    private fun Extension<MinixPlugin>.getModule(): org.koin.core.module.Module? {
        if (bound) return null

        val module = module {
            val binds = arrayListOf<KClass<*>>(this@getModule::class).also { if (bindToKClass != null) it.add(bindToKClass!!) }
            single { this@getModule }.binds(binds.toTypedArray())
        }

        return module
    }

    private suspend fun Extension<MinixPlugin>.errorDependents(
        extensions: MutableList<Extension<MinixPlugin>>,
        error: Throwable? = null
    ) {
        val dependents = getAllDependents(this, extensions.reversed(), hashSetOf())
        dependents.forEach { it.setState(ExtensionState.FAILED_DEPENDENCIES) }

        log.error(error) {
            val builder = StringBuilder()
            builder.append("There was an error while loading / enabling extension ${this::class.getFullName()}!")
            builder.append("\nThis is not a fatal error, but it may cause other extensions to fail to load.")
            if (dependents.isNotEmpty()) {
                builder.append("\nThese extensions will not be loaded:")
                dependents.forEach { builder.append("\n\t${it.name}") }
            }
            builder.toString()
        }
    }

    // TODO: Why oh why is this not working? Please somehow figure out how to get the dependencies of dependencies and so on.
    private suspend fun Extension<MinixPlugin>.extensions(
        extensions: MutableList<Extension<MinixPlugin>>,
        dependents: MutableList<Extension<MinixPlugin>>
    ): MutableList<Extension<MinixPlugin>> {
        for (extension in extensions.reversed()) {
            if (bindToKClass !in extension.dependencies &&
                this::class !in extension.dependencies ||
                extension.dependencies.all { clazz -> dependents.any { it.bindToKClass == clazz || it::class == clazz } }
            ) continue

            dependents.add(extension)
            extension.setState(ExtensionState.FAILED_DEPENDENCIES)
        }
        return dependents
    }

    private suspend inline fun <reified P : MinixPlugin> P.shutdownInOrder() {
        val cache = pluginCache[this]
        cache.loadedExtensions.reverse()
        cache.loadedExtensions.removeAll { ex ->
            log.debug { "Unloading extension ${ex.name}" }
            runBlocking {
                ex.setState(ExtensionState.UNLOADING)
                try {
                    withTimeout(5.seconds) {
                        ex.eventListener.unregisterListener()
                        // Give the jobs 2 seconds of grace time to unload then shutdown forcefully
                        try {
                            withTimeoutOrNull(2.seconds) {
                                ex.supervisor.coroutineContext.job.unsafeCast<CompletableJob>().complete()
                            }
                        } catch (e: TimeoutCancellationException) { ex.supervisor.cancel(e) }
                        ex.invokeIfOverrides(Extension<*>::handleUnload.name) { ex.handleUnload() }
                    }
                } catch (e: Throwable) {
                    if (e is TimeoutCancellationException) {
                        log.warn { "Extension ${ex.name} took too longer than 5 seconds to unload!" }
                    } else log.error(e) { "Extension ${ex.name} through an error while unloading!" }
                    ex.setState(ExtensionState.FAILED_UNLOADING)
                }
                unloadKoinModules(module { single { ex } bind (ex.bindToKClass ?: ex::class).unsafeCast() })
                ex.setState(ExtensionState.UNLOADED)
                cache.unloadedExtensions += ex
            }
            true
        }
    }

    companion object {

        fun Extension<*>.dependsOn(other: Extension<*>): Boolean {
            if (this.dependencies.isEmpty()) return false
            return this.dependencies.any { it == other.bindToKClass || it == other::class }
        }

        fun getAllDependents(
            requiredExt: Extension<*>,
            allExtensions: List<Extension<MinixPlugin>>,
            currentDependents: HashSet<Extension<*>>
        ): HashSet<Extension<*>> {
            for (extension in allExtensions) {
                if (extension.dependsOn(requiredExt) && extension !in currentDependents) {
                    currentDependents += extension
                    currentDependents += getAllDependents(extension, allExtensions, currentDependents)
                }
            }
            return currentDependents
        }
    }
}
