package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.reflection.OverrideUtils
import dev.racci.minix.core.MinixApplicationBuilder
import dev.racci.minix.core.MinixImpl
import dev.racci.minix.core.MinixInit
import dev.racci.minix.core.coroutine.service.CoroutineSessionImpl
import dev.racci.minix.core.services.mapped.ConfigurationMapper
import dev.racci.minix.core.services.mapped.ExtensionMapper
import io.github.classgraph.ClassGraph
import kotlinx.coroutines.runBlocking
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.PluginClassLoader
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.full.findAnnotation

@MappedExtension(Minix::class, "Plugin Manager", bindToKClass = PluginService::class)
@OptIn(MinixInternal::class)
class PluginServiceImpl(override val plugin: Minix) : PluginService, Extension<Minix>() {
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

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            if (plugin.version.isPreRelease) {
                logger.warn { "Plugin ${plugin.name} is a pre-release version and may not be stable." }
                plugin.log.setLevel(MinixLogger.LoggingLevel.TRACE)
                plugin.log.lockLevel()
            }

            loadDependencies(plugin)

            loadKoinModules(KoinUtils.getModule(plugin))
            logRunning(plugin, plugin::handleLoad)

            loadReflection(plugin)

            get<ExtensionMapper>().loadExtensions(plugin)

            logRunning(plugin, plugin::handleAfterLoad)
        }
    }

    override fun startPlugin(plugin: MinixPlugin) {
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            logRunning(plugin, plugin::handleEnable)

            registerStats(plugin)

            get<ExtensionMapper>().enableExtensions(plugin)

            logRunning(plugin, plugin::handleAfterEnable)
            loadedPlugins += plugin::class to plugin
        }
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadedPlugins -= plugin::class
            val isFullUnload = isFullUnload()

            logger.debug { "Unloading plugin ${plugin.name} (full unload: $isFullUnload)" }

            logRunning(plugin, plugin::handleDisable)
            if (isFullUnload) logRunning(plugin, plugin::handleUnload)

            pluginCache.getIfPresent(plugin)?.apply {
                val dataService = get<DataServiceImpl>()
                val extensionMapper = get<ExtensionMapper>()

                dataService.configDataHolder.invalidateAll(this.configurations)
                extensionMapper.disableExtensions(plugin)
                if (isFullUnload) extensionMapper.unloadExtensions(plugin)
            }

            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                logger.trace { "Cancelling ${it.size} tasks." }
                it.forEach { id -> CoroutineScheduler.shutdownTask(id) }
            }

            if (!isFullUnload) return@runBlocking
            coroutineService.disable(plugin)
            unloadKoinModules(KoinUtils.getModule(plugin))
            pluginCache.invalidate(plugin)
        }
    }

    override fun reloadPlugin(plugin: MinixPlugin) {
        val cache = pluginCache[plugin] ?: return
        get<DataServiceImpl>().configDataHolder.refreshAll(cache.configurations) // Loads data from disk without saving.
    }

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

    override suspend fun fromClassloader(classLoader: ClassLoader): MinixPlugin? {
        for (plugin in loadedPlugins.values) {
            if (pluginCache[plugin].getClassLoader() != classLoader) continue
            return plugin
        }
        return null
    }

    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin].castOrThrow()

    private fun isFullUnload(): Boolean = server.isStopping || pluginCache.getIfPresent(plugin)?.wantsFullUnload == true

    private suspend fun logRunning(
        plugin: MinixPlugin,
        func: KSuspendFunction0<Unit>
    ) {
        if (!OverrideUtils.doesOverrideFunction(plugin::class, func)) return

        logger.trace { "Running [${plugin.name}:${func.name}]." }
        func.invoke()
    }

    private fun registerStats(plugin: MinixPlugin) {
        val id = this::class.findAnnotation<MappedPlugin>()?.bStatsId

        if (id == null || id == 0) return

        logger.info { "Registering bStats." }
        pluginCache[plugin].metrics = Metrics(plugin, id)
    }

    private suspend fun loadReflection(plugin: MinixPlugin) {
        var int = 0
        val scanResult = ClassGraph()
            .acceptJars(plugin::class.java.protectionDomain.codeSource.location.path.substringAfterLast('/'))
            .addClassLoader(plugin::class.java.classLoader)
            .addClassLoader(pluginCache[plugin].getClassLoader())
            .enableClassInfo()
            .enableAnnotationInfo()
            .disableNestedJarScanning()
            .disableRuntimeInvisibleAnnotations()
            .rejectClasses(
                PluginServiceImpl::class.qualifiedName,
                ExtensionMapper::class.qualifiedName
            )
            .scan(4)

        if (plugin !is MinixImpl) plugin.log.info { "Ignore the following warning, this is expected behavior." }

        get<ExtensionMapper>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))
        get<ConfigurationMapper>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))
        get<IntegrationService>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))

        scanResult.close()
    }

    private suspend fun loadDependencies(plugin: MinixPlugin) {
        val classLoader = pluginCache[plugin].getClassLoader()
        logger.debug { "Loading dependencies for ${plugin.name}, with class ${plugin::class}, with loader ${classLoader.name}" }

        if (MinixApplicationBuilder.createApplication(plugin) == null) {
            logger.debug { "Plugin ${plugin.name} does not have any needed libraries." }
            return
        }
    }
}
