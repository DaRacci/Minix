package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionState
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.extensions.unregisterListener
import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.integrations.IntegrationManager
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.plugin.SusPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.collections.CollectionUtils.clear
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.api.utils.kotlin.ifOverrides
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import dev.racci.minix.api.utils.kotlin.invokeIfOverrides
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import dev.racci.minix.core.MinixImpl
import dev.racci.minix.core.MinixInit
import dev.racci.minix.core.coroutine.service.CoroutineSessionImpl
import io.github.classgraph.AnnotationClassRef
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.ext.getFullName
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@MappedExtension(Minix::class, "Plugin Manager", bindToKClass = PluginService::class)
@OptIn(MinixInternal::class)
class PluginServiceImpl(override val plugin: Minix) : PluginService, Extension<Minix>() {

    private val dataService by inject<DataServiceImpl>()
    private val integrationService by inject<IntegrationService>()

    override val coroutineSession: LoadingCache<MinixPlugin, CoroutineSession> = Caffeine.newBuilder().build { plugin ->
        if (!plugin.isEnabled) {
            throw plugin.log.fatal(RuntimeException()) {
                """
                Plugin ${plugin.name} attempted to start a new coroutine session while being disabled.
                Dispatchers such as plugin.minecraftDispatcher and plugin.asyncDispatcher are already
                disposed of at this point and cannot be used.
                """.trimIndent()
            }
        }
        CoroutineSessionImpl(plugin)
    }

    override val loadedPlugins by lazy { mutableMapOf<KClass<out MinixPlugin>, MinixPlugin>() }

    override val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>> = Caffeine.newBuilder().build(::PluginData)

    @OptIn(ExperimentalStdlibApi::class)
    override fun firstNonMinixPlugin(): MinixPlugin? {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk { stream ->
                stream.filter { frame ->
                    val loader = frame.declaringClass.classLoader
                    loader is PluginClassLoader && loader.plugin !is MinixImpl && loader.plugin !is MinixInit
                }.map { frame ->
                    val classLoader = frame.declaringClass.classLoader as PluginClassLoader
                    classLoader.plugin as MinixPlugin
                }.findFirst()
            }.getOrNull()
    }

    override fun fromClassloader(classLoader: ClassLoader): MinixPlugin? {
        for (plugin in loadedPlugins.values) {
            if (pluginCache[plugin].loader != classLoader) continue
            return plugin
        }
        return null
    }

    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin].unsafeCast()

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            plugin.ifOverrides(SusPlugin::handleLoad) {
                plugin.log.trace { "Running HandleLoad." }
                plugin.handleLoad()
            }

            loadKoinModules(getModule(plugin))

            if (plugin.version.isPreRelease) {
                plugin.log.setLevel(MinixLogger.LoggingLevel.TRACE)
                plugin.log.lockLevel()
            }

            loadReflection(plugin)
            loadExtensions(plugin)

            plugin.ifOverrides(SusPlugin::handleAfterLoad) {
                plugin.log.trace { "Running HandleAfterLoad." }
                plugin.handleAfterLoad()
            }
        }
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadedPlugins -= plugin::class

            val isFullUnload = server.isStopping // TODO -> Support ServerUtils
            if (isFullUnload) {
                plugin.log.debug { "Running full unload." }
            }

            plugin.ifOverrides(SusPlugin::handleDisable) {
                plugin.log.trace { "Running handleDisable." }
                plugin.handleDisable()
            }

            if (isFullUnload && plugin::class.doesOverride(SusPlugin::handleUnload)) {
                plugin.log.trace { "Running handleUnload." }
                plugin.handleUnload()
            }

            val cache = pluginCache.getIfPresent(plugin)

            cache?.configurations?.takeIf(MutableList<*>::isNotEmpty)?.let { configs ->
                plugin.log.trace { "Unloading ${configs.size} configurations." }
                dataService.configDataHolder.invalidateAll(configs)
            }

            cache?.extensions?.takeIf(MutableList<*>::isNotEmpty)?.let { ex ->
                plugin.log.trace { "Disabling ${ex.size} extensions." }
                disableExtensions(plugin)

                if (isFullUnload) {
                    plugin.log.trace { "Unloading ${ex.size} extensions." }
                    unloadExtensions(plugin)
                }
            }

            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                plugin.log.trace { "Cancelling ${it.size} tasks." }
                it.forEach { id -> CoroutineScheduler.cancelTask(id) }
            }

            if (!isFullUnload) return@runBlocking
            coroutineService.disable(plugin)
            unloadKoinModules(getModule(plugin))
        }
    }

    override fun startPlugin(plugin: MinixPlugin) {
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            val cache = pluginCache[plugin]

            plugin.invokeIfOverrides(SusPlugin::handleEnable.name) {
                plugin.log.trace { "Running handleEnable." }
                plugin.handleEnable()
            }

            plugin.bStatsId.invokeIfNotNull {
                plugin.log.info { "Registering bStats." }
                cache.metrics = Metrics(plugin, it)
            }

            enableExtensions(plugin)

            plugin.invokeIfOverrides(SusPlugin::handleAfterEnable.name) {
                plugin.log.trace { "Running handleAfterEnable." }
                plugin.handleAfterEnable()
            }

            loadedPlugins += plugin::class to plugin
        }
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    private fun checkAddedAndMissingDeps(
        plugin: MinixPlugin,
        next: Extension<out MinixPlugin>,
        sortedExtensions: MutableList<Extension<out MinixPlugin>>,
        extensions: ArrayDeque<Extension<out MinixPlugin>>
    ): Boolean {
        if (next !in sortedExtensions) return false

        plugin.log.debug { "Missing dependency for ${next.name} in ${plugin.name}, Reordering required dependencies." }
        val currentIndex = sortedExtensions.indexOf(next)
        sortedExtensions.removeAt(currentIndex)

        val needed = next::class.findAnnotation<MappedExtension>()!!.dependencies
            .filterNot { sortedExtensions.any { ex -> ex::class == it } }
            .mapNotNull { clazz -> extensions.find { it::class == clazz || bindToKClass(it) == clazz } }

        sortedExtensions.addAll(currentIndex, needed)
        sortedExtensions.add(currentIndex + needed.size + 1, next)
        return true
    }

    private fun bindToKClass(instance: Any): KClass<*> {
        return when (instance) {
            is Extension<*> -> instance::class.findAnnotation<MappedExtension>()!!.bindToKClass.takeUnless { it == Extension::class } ?: instance::class
            is MinixPlugin -> instance::class.findAnnotation<MappedPlugin>()!!.bindToKClass.takeUnless { it == MinixPlugin::class } ?: instance::class
            else -> instance::class
        }
    }

    private fun checkNoDeps(
        plugin: MinixPlugin,
        next: Extension<out MinixPlugin>,
        sortedExtensions: MutableList<Extension<out MinixPlugin>>
    ): Boolean {
        val annotation = next::class.findAnnotation<MappedExtension>()!!
        if (next in sortedExtensions ||
            annotation.dependencies.isNotEmpty() &&
            annotation.dependencies.all { dep -> sortedExtensions.find { it::class == dep } == null }
        ) return false

        plugin.log.trace { "All dependencies for ${next.name} are loaded, adding to sorted" }
        return sortedExtensions.add(next)
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

    private suspend fun errorDependents(
        failedExtension: Extension<*>,
        extensions: List<Extension<*>>,
        error: Throwable? = null
    ) {
        val dependents = getRecursiveDependencies(failedExtension, extensions.reversed(), hashSetOf())
        dependents.forEach { unloadExtension(it) }

        log.error(error) {
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

    internal fun getModule(instance: Any): org.koin.core.module.Module {
        val module = module {
            val bind = bindToKClass(instance)
            val binds = setOf(instance::class, bind).toTypedArray()
            single { instance }.binds(binds)
        }

        return module
    }

    private fun sortInitialized(plugin: MinixPlugin): MutableList<Extension<out MinixPlugin>> {
        val cache = pluginCache[plugin]
        val extensions = ArrayDeque(cache.extensions.filter { it.state == ExtensionState.UNLOADED })
        val sortedExtensions = mutableListOf<Extension<out MinixPlugin>>()

        while (extensions.isNotEmpty()) {
            val popped = extensions.removeFirst()

            if (checkNoDeps(plugin, popped, sortedExtensions)) continue
            if (checkAddedAndMissingDeps(plugin, popped, sortedExtensions, extensions)) continue
            checkOrder(plugin, popped, sortedExtensions, extensions)
        }

        return sortedExtensions
    }

    private suspend fun loadExtensions(plugin: MinixPlugin) {
        val sorted = sortInitialized(plugin)
        for (extension in sorted) { loadExtension(extension, sorted) }
    }

    internal suspend fun loadExtension(
        extension: Extension<*>,
        sorted: MutableList<Extension<*>>
    ) {
        withState(ExtensionState.LOADING, extension) {
            extension.handleLoad()
        }.onFailure {
            errorDependents(extension, sorted, it)
            unloadExtension(extension)
        }
    }

    private suspend fun unloadExtensions(plugin: MinixPlugin) {
        val cache = pluginCache[plugin]
        cache.extensions.asReversed().clear { unloadExtension(it) }
    }

    private suspend fun unloadExtension(extension: Extension<*>) {
        if (extension.state.ordinal <= 5) {
            log.trace { "Unloading extension ${extension.name}." }

            withState(ExtensionState.UNLOADING, extension) {
                extension.handleUnload()
            }.fold(
                { log.trace { "Unloaded extension ${extension.name}." } },
                { log.error(it) { "Extension ${extension.name} threw an error while unloading!" } }
            )

            extension.eventListener.unregisterListener()
            extension.supervisor.coroutineContext.job.unsafeCast<CompletableJob>().complete()
            extension.dispatcher.close()
            pluginCache[extension.plugin].extensions.remove(extension)
            unloadKoinModules(getModule(extension))
        }
    }

    private suspend fun enableExtensions(plugin: MinixPlugin) {
        val extensions = pluginCache[plugin].extensions
        for (extension in extensions) {
            if (extension.state.ordinal < 2 || extension.state.ordinal > 3) continue

            withState(ExtensionState.ENABLED, extension) {
                extension.handleEnable()
            }.fold(
                { log.trace { "Enabled extension ${extension.name}." } },
                {
                    errorDependents(extension, extensions, it)
                    unloadExtension(extension)
                }
            )
        }
    }

    private suspend fun disableExtensions(plugin: MinixPlugin) {
        val extensions = pluginCache[plugin].extensions.asReversed()
        for (extension in extensions) {
            if (extension.state.ordinal < 4) continue

            withState(ExtensionState.DISABLING, extension) {
                extension.handleDisable()
            }.fold(
                { log.trace { "Disabled extension ${extension.name}." } },
                {
                    errorDependents(extension, extensions, it)
                    unloadExtension(extension)
                }
            )
        }
    }

    private suspend fun withState(
        state: ExtensionState,
        extension: Extension<*>,
        block: suspend () -> Unit
    ): Result<Boolean> {
        extension.setState(state)

        try {
            block()
        } catch (e: Throwable) {
            log.error(e) { "Extension ${extension.name} threw an error while ${state.name.lowercase()}!" }
            return Result.failure(e)
        }

        extension.setState(ExtensionState.values()[state.ordinal - 1])
        return Result.success(true)
    }

    private fun getClassLoader(plugin: MinixPlugin): ClassLoader {
        var loader = pluginCache.getIfPresent(plugin)?.loader
        if (loader != null) return loader

        val property = JavaPlugin::class.declaredMemberProperties.first { it.name == "classLoader" }
        property.isAccessible = true
        loader = property.getter.call(plugin).unsafeCast()
        property.isAccessible = false
        return loader!!
    }

    private fun loadReflection(plugin: MinixPlugin) {
        var int = 0
        val packageName = plugin::class.java.`package`.name.takeWhile { it != '.' || int++ < 2 }
        val classGraph = ClassGraph()
            .acceptPackages(packageName)
            .addClassLoader(plugin::class.java.classLoader)
            .addClassLoader(getClassLoader(plugin))
            .enableClassInfo()
            .enableAnnotationInfo()
            .enableMethodInfo()
            .scan()

        if (plugin !is MinixImpl) plugin.log.info { "Ignore the following warning, this is expected behavior." }
        val boundKClass = plugin::class.findAnnotation<MappedPlugin>()?.bindToKClass.takeUnless { it == MinixPlugin::class } ?: plugin::class

        processMappedExtensions(classGraph, plugin, boundKClass)
        processMappedConfigurations(classGraph, plugin, boundKClass)
        processMappedIntegrations(classGraph, plugin, boundKClass)
    }

    private inline fun <reified T : Annotation> matchingAnnotation(
        plugin: MinixPlugin,
        boundKClass: KClass<*>,
        clazz: ClassInfo
    ): Boolean {
        val annotation = clazz.getAnnotationInfo(T::class.java)
        val classRef = annotation.parameterValues["parent"].value.safeCast<AnnotationClassRef>()?.loadClass()?.kotlin
        val result = annotation.parameterValues["parent"].value is AnnotationClassRef && classRef == plugin::class || classRef == boundKClass

        if (!result) {
            plugin.log.error { "Found ${T::class.simpleName} [${clazz.fullyQualifiedDefiningMethodName} but it is not bound to this plugin." }
        }

        return result
    }

    private fun processMappedConfigurations(
        scanResult: ScanResult,
        plugin: MinixPlugin,
        boundKClass: KClass<out Any>
    ) {
        for (classInfo in scanResult.getClassesWithAnnotation(MappedConfig::class.java)) {
            if (!matchingAnnotation<MappedConfig>(plugin, boundKClass, classInfo)) continue

            plugin.log.trace { "Registered MappedIntegration [${classInfo.simpleName}]." }

            try {
                dataService.configDataHolder.get(classInfo.loadClass().kotlin.unsafeCast())
            } catch (ignored: NoBeanDefFoundException) {
                /* This is expected behavior. */
            } catch (e: Exception) {
                throw this.plugin.log.fatal(e) { "Failed to create configuration [${classInfo.simpleName}] for ${plugin.name}" }
            }
        }
    }

    private fun processMappedExtensions(
        scanResult: ScanResult,
        plugin: MinixPlugin,
        boundKClass: KClass<out Any>
    ) {
        for (classInfo in scanResult.getClassesWithAnnotation(MappedExtension::class.java)) {
            if (!matchingAnnotation<MappedExtension>(plugin, boundKClass, classInfo)) continue

            if (!classInfo.extendsSuperclass(Extension::class.java)) {
                plugin.log.error { "Class ${classInfo.name} is annotated with @MappedExtension but does not extend Extension." }
                continue
            }

            plugin.log.trace { "Registering MappedExtension [${classInfo.fullyQualifiedDefiningMethodName}]." }

            val kClass = classInfo.loadClass().kotlin.unsafeCast<KClass<Extension<*>>>()
            val constructor = kClass.constructors.first { it.parameters.isEmpty() || it.parameters[0].name == "plugin" }

            val extension = when (constructor.parameters.size) {
                0 -> constructor.call()
                else -> constructor.call(plugin)
            }

            loadKoinModules(getModule(extension))
            pluginCache[plugin].extensions.add(extension)
        }
    }

    private fun processMappedIntegrations(
        scanResult: ScanResult,
        plugin: MinixPlugin,
        boundKClass: KClass<*>
    ) {
        for (classInfo in scanResult.getClassesWithAnnotation(MappedIntegration::class.java)) {
            if (!matchingAnnotation<MappedIntegration>(plugin, boundKClass, classInfo)) continue

            plugin.log.trace { "Registering MappedIntegration [${classInfo.simpleName}]." }

            val kClass = classInfo.loadClass().kotlin.unsafeCast<KClass<out Integration>>()
            val annotation = kClass.findAnnotation<MappedIntegration>()!!
            val managerKClass = annotation.IntegrationManager
            val manager = managerKClass.objectInstance.safeCast<IntegrationManager<Integration>>()

            if (manager == null) {
                plugin.log.error { "Failed to obtain singleton instance of ${managerKClass.qualifiedName}." }
                continue
            }

            IntegrationService.IntegrationLoader(annotation.pluginName) {
                val constructor = kClass.primaryConstructor!!
                when (constructor.parameters.size) {
                    0 -> constructor.call()
                    else -> constructor.call(get(StringQualifier(plugin.name)))
                }
            }.let(integrationService::registerIntegration)
        }
    }

    private fun getRecursiveDependencies(
        requiredExt: Extension<*>,
        allExtensions: List<Extension<*>>,
        currentDependents: HashSet<Extension<*>>
    ): HashSet<Extension<*>> {
        for (extension in allExtensions) {
            val annotation = extension::class.findAnnotation<MappedExtension>()!!
            if (annotation.dependencies.isEmpty()) continue
            if (annotation.dependencies.none { it == bindToKClass(requiredExt) || it == requiredExt::class }) continue
            if (extension in currentDependents) continue

            currentDependents += extension
            currentDependents += getRecursiveDependencies(extension, allExtensions, currentDependents)
        }

        return currentDependents
    }
}
