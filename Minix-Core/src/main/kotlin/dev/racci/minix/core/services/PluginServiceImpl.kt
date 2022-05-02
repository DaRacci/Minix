package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.coroutine.registerSuspendingEvents
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
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
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.koin.dsl.bind
import org.koin.dsl.module
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
                    "Plugin ${plugin.name} attempt to start a new coroutine session while being disabled. " +
                        "Dispatchers such as plugin.minecraftDispatcher and plugin.asyncDispatcher are already " +
                        "disposed at this point and cannot be used!"
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

            loadModule { single { plugin } bind (plugin.bindToKClass ?: plugin::class) }
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

            cache.listeners.ifNotEmpty { collection ->
                plugin.log.debug { "Registering ${collection.size} listeners for ${plugin.name}" }
                collection.forEach(plugin::registerSuspendingEvents)
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
            if (next !in sortedExtensions &&
                (
                    next.dependencies.isEmpty() ||
                        next.dependencies.all { dep -> sortedExtensions.find { it::class == dep } != null }
                    )
            ) {
                log.debug { "All dependencies for ${next.name} are loaded, adding to sorted" }
                sortedExtensions.add(next)
                continue
            }

            if (next in sortedExtensions &&
                next.dependencies.any { dep -> sortedExtensions.find { it::class == dep } == null }
            ) {
                log.debug { "Dependency for ${next.name} is not loaded, reordering needed deps." }
                val index = sortedExtensions.indexOf(next)
                log.debug { "Index of ${next.name} is $index" }
                sortedExtensions.remove(next)
                val neededDepends = next.dependencies.filter { dep -> sortedExtensions.find { it::class == dep } == null }.map { exKClass -> extensions.find { it::class == exKClass }!! }
                log.debug { "Needed depends for ${next.name} are ${neededDepends.joinToString { it.name }}" }
                sortedExtensions.addAll(index, neededDepends)
                sortedExtensions.add(index + neededDepends.size + 1, next)
                log.debug { "New index of ${next.name} is ${sortedExtensions.indexOf(next)}" }
                continue
            }

            for (dependency in next.dependencies) {
                if (sortedExtensions.find { it::class == dependency } != null) {
                    log.debug { "Dependency $dependency for ${next.name} is in sorted, skipping." }
                    continue
                }
                extensions.find { it::class == dependency }?.let {
                    sortedExtensions.add(it)
                    log.debug { "Adding ${it.name} to sorted before ${next.name}" }
                }
            }
            // TODO: i shouldn't need another check here
            if (next !in sortedExtensions) {
                log.debug { "Adding ${next.name} to sorted" }
                sortedExtensions.add(next)
            } else log.debug { "Extension ${next.name} is already in sorted, skipping." }
        }
        pluginCache[this].loadedExtensions.clear()
        return sortedExtensions
    }

    // TODO: Refactor the dependency system to be more efficient and less hacky maybe using a shared flow
    private suspend fun MinixPlugin.loadInOrder() {
        val cache = pluginCache[this]
        val sorted = getSortedExtensions()
        sorted.forEach { ex ->
            val module = module {
                single { ex } bind (ex.bindToKClass ?: ex::class)
                if (ex.bindToKClass != null) { // Bind to both types
                    single { ex.bindToKClass } bind (ex::class)
                }
            }
            loadKoinModules(module)
            ex.setState(ExtensionState.LOADING)
            try {
                withTimeout(5.seconds) {
                    ex.invokeIfOverrides(Extension<*>::handleLoad.name) {
                        log.info { "Loading extension ${ex.name}" }
                        ex.handleLoad()
                    }
                }
            } catch (e: Throwable) {
                if (e is TimeoutCancellationException) { log.warn { "Extension ${ex.name} took too longer than 5 seconds to load!" } }
                ex.setState(ExtensionState.FAILED_LOADING)
                ex.errorDependents(sorted, e)
                unloadKoinModules(module)
            }
            ex.setState(ExtensionState.LOADED)
            cache.loadedExtensions += ex
        }
        cache.extensions.clear()
    }

    // TODO: Look into failed dependencies not being respected
    private suspend fun MinixPlugin.startInOrder() {
        val cache = pluginCache[this]
        val sorted = getSortedExtensions()
        sorted
            .filter {
                var load = false
                when (it.state) {
                    ExtensionState.FAILED_DEPENDENCIES -> log.warn { "Extension ${it.name} had one or more dependencies fail to load, Skipping." }
                    ExtensionState.FAILED_LOADING -> log.warn { "Extension ${it.name} had previously failed to load, Skipping." }
                    ExtensionState.FAILED_UNLOADING -> log.warn { "Extension ${it.name} had previously failed to unload, Skipping." }
                    ExtensionState.LOADING, ExtensionState.ENABLING, ExtensionState.UNLOADING -> log.warn { "Extension ${it.name} is still ${it.state.name.lowercase()}, Skipping." }
                    else -> load = true
                }
                if (!load) {
                    it.errorDependents(sorted)
                }
                load
            }
            .forEach { ex ->
                val module = module { single { ex } bind (ex.bindToKClass ?: ex::class) }
                if (!ex.bound) { loadKoinModules(module) }
                ex.setState(ExtensionState.ENABLING)
                try {
                    withTimeout(5.seconds) {
                        ex.invokeIfOverrides(Extension<*>::handleEnable.name) {
                            log.info { "Enabling extension ${ex.name}" }
                            ex.handleEnable()
                        }
                    }
                } catch (e: Throwable) {
                    if (e is TimeoutCancellationException) { log.warn { "Extension ${ex.name} took too longer than 5 seconds to enable!" } }
                    ex.setState(ExtensionState.FAILED_ENABLING)
                    ex.errorDependents(sorted, e)
                    unloadKoinModules(module)
                }
                ex.setState(ExtensionState.ENABLED)
                cache.loadedExtensions += ex
            }
        cache.extensions.clear()
    }

    private suspend fun Extension<MinixPlugin>.errorDependents(
        extensions: MutableList<Extension<MinixPlugin>>,
        error: Throwable? = null
    ) {
        val deps = extensions.filter { this::class in it.dependencies }
        log.error(error) {
            val builder = StringBuilder()
            builder.append("There was an error while loading / enabling extension ${this.name}!")
            builder.append("\n\t\tThis is not a fatal error, but it may cause other extensions to fail to load.")
            if (deps.isNotEmpty()) {
                builder.append("\n\t\tThese extensions will not be loaded:")
                deps.forEach { builder.append("\n\t\t\t${it.name}") }
            }
            builder.toString()
        }
        deps.forEach {
            it.setState(ExtensionState.FAILED_DEPENDENCIES)
        }
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
                        ex.invokeIfOverrides(Extension<*>::handleUnload.name) { ex.handleUnload() }
                    }
                } catch (e: Throwable) {
                    if (e is TimeoutCancellationException) {
                        log.warn { "Extension ${ex.name} took too longer than 5 seconds to unload!" }
                    } else log.error(e) { "Extension ${ex.name} through an error while unloading!" }
                    ex.setState(ExtensionState.FAILED_UNLOADING)
                }
                unloadKoinModules(module { single { ex } bind (ex.bindToKClass ?: ex::class) }) // TODO: This is a bit of a hack, but it works for now.
                ex.setState(ExtensionState.UNLOADED)
                cache.unloadedExtensions += ex
            }
            true
        }
    }
}
