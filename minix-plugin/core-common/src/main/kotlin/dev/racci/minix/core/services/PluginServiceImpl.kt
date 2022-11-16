package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.DoNotUnload
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.collections.findKProperty
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.reflection.OverrideUtils
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.mapped.ConfigurationMapper
import dev.racci.minix.core.services.mapped.ExtensionMapper
import dev.racci.minix.core.services.mapped.IntegrationMapper
import io.github.classgraph.ClassGraph
import io.github.toolfactory.jvm.Driver
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPluginLoader
import org.bukkit.plugin.java.PluginClassLoader
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

@DoNotUnload
@MappedExtension([ExtensionMapper::class, ConfigurationMapper::class, IntegrationMapper::class], PluginService::class)
class PluginServiceImpl(override val plugin: Minix) : PluginService, Extension<Minix>() {
    private lateinit var driver: Driver

    override val loadedPlugins: MutableMap<KClass<out MinixPlugin>, MinixPlugin> by lazy { mutableMapOf() }

    override suspend fun handleLoad() {
        driver = Driver.Factory.getNew()
    }

    override suspend fun handlePostUnload() {
        driver.close()
    }

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            plugin.scope.declare(
                get<CoroutineSession> { parametersOf(plugin) },
                allowOverride = false
            )

            if (plugin.version.isPreRelease) {
                logger.warn { "Plugin ${plugin.value} is a pre-release version and may not be stable." }
                plugin.logger.setLevel(LoggingLevel.TRACE)
                plugin.logger.lockLevel()
            }

            loadDependencies(plugin)

            if (plugin !is Minix) loadKoinModules(KoinUtils.getModule(plugin))

            logRunning(plugin, plugin::handleLoad)

            loadReflection(plugin)

            get<ExtensionMapper>().loadExtensions(plugin)

            logRunning(plugin, plugin::handlePostLoad)
        }
    }

    override fun enablePlugin(plugin: MinixPlugin) {
        plugin.scope.get<CoroutineSession>().withManipulatedServerHeartBeat {
            logRunning(plugin, plugin::handleEnable)

            get<ExtensionMapper>().enableExtensions(plugin)

            logRunning(plugin, plugin::handlePostEnable)
            loadedPlugins += plugin::class to plugin
        }
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadedPlugins -= plugin::class
            val isFullUnload = true // isFullUnload()

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

            if (isFullUnload) forgetPlugin(plugin)
        }
    }

    private suspend fun forgetPlugin(plugin: MinixPlugin) {
        val cache = pluginCache[plugin]

        get<ExtensionMapper>().forgetMapped(plugin, cache)
        get<IntegrationMapper>().forgetMapped(plugin, cache)
        get<ConfigurationMapper>().forgetMapped(plugin, cache)

        coroutineSession.getIfPresent(plugin)?.dispose()
        pluginCache.invalidate(plugin)

        unloadKoinModules(KoinUtils.getModule(plugin))
        KoinUtils.clearBinds(plugin)

        loadedPlugins -= plugin::class
        PluginDependentMinixLogger.EXISTING -= plugin

        cleanClasses(plugin, pluginCache[this@PluginServiceImpl.plugin].getClassLoader())
    }

    private suspend fun cleanClasses(
        of: MinixPlugin,
        from: PluginClassLoader
    ) {
        PluginClassLoader::class.declaredMemberProperties.findKProperty<Map<String, Class<Any>>>("classes")
            .map { it.accessGet(from) }
            .orNull()!!.filter { it.key.contains(of.name) }
            .forEach { logger.debug { "Classes contains ${it.key}" } }

        val loader = PluginClassLoader::class.declaredMemberProperties.findKProperty<JavaPluginLoader>("loader")
            .map { it.accessGet(from) }
            .orNull()!!

        JavaPluginLoader::class.declaredMemberProperties.findKProperty<Map<String, *>>("classLoadLock")
            .map { it.accessGet(loader) }
            .orNull()!!.filter { it.key.contains(of.name) }
            .forEach { logger.debug { "ClassLoadLock contains ${it.key}" } }

        JavaPluginLoader::class.declaredMemberProperties.findKProperty<Map<String, *>>("classLoadLockCount")
            .map { it.accessGet(loader) }
            .orNull()!!.filter { it.key.contains(of.name) }
            .forEach { logger.debug { "classLoadLockCount contains ${it.key}" } }

        JavaPluginLoader::class.declaredMemberProperties.findKProperty<List<PluginClassLoader>>("loaders")
            .map { it.accessGet(loader) }
            .orNull()!!.filter { it.name == of.name }
            .forEach { logger.debug { "Loaders contains $it" } }

        val classMap = PluginClassLoader::class.declaredMemberProperties.findKProperty<Set<String>>("seenIllegalAccess")
            .map { it.accessGet(from) }
            .map { it to it::class.declaredMemberProperties.findKProperty<ConcurrentHashMap<String, Boolean>>("m").orNull()!! }
            .tap { driver.setAccessible(it.second.javaField, true) }
            .map { it.second.get(it.first) }
            .tap { it.remove(of.name) }
            .orNull() ?: throw logger.fatal { "Could not find classes property in plugin class loader." }

        ClassGraph().addClassLoader(from)
            .disableDirScanning()
            .disableModuleScanning()
            .disableNestedJarScanning()
            .removeTemporaryFilesAfterScan()
            .disableRuntimeInvisibleAnnotations()
            .acceptJars(of::class.java.protectionDomain.codeSource.location.path.substringAfterLast('/'))
            .scan(4)
            .allClasses
            .onEach { logger.trace { "Found class ${it.name} in plugin ${of.name}." } }
//            .filter { info ->
//                classMap.contains(info)
//            }
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

    override fun fromClassloader(classLoader: ClassLoader): MinixPlugin? {
        for (plugin in loadedPlugins.values) {
            if (pluginCache[plugin].getClassLoader() != classLoader) continue
            return plugin
        }
        return null
    }

    public override fun getValue(thisRef: Any, property: Any?): MinixPlugin {
        return this.fromClassLoader(thisRef::class.java.classLoader)!!
    }

    private fun isFullUnload(): Boolean = server.isStopping || pluginCache.getIfPresent(plugin)?.wantsFullUnload == true

    private suspend fun logRunning(
        plugin: MinixPlugin,
        func: KSuspendFunction0<Unit>
    ) {
        if (!OverrideUtils.doesOverrideFunction(plugin::class, func)) {
            return logger.debug { "${plugin.value} doesn't override function ${func.name}, skipping." }
        }

        logger.trace { "Running [${plugin.value}:${func.name}]." }
        func.invoke()
    }

    private suspend fun loadReflection(plugin: MinixPlugin) {
        val scanResult = ClassGraph()
            .acceptJars(plugin::class.java.protectionDomain.codeSource.location.path.substringAfterLast('/'))
            .addClassLoader(plugin::class.java.classLoader)
            .addClassLoader(pluginCache[plugin].getClassLoader())
            .enableClassInfo()
            .enableAnnotationInfo()
            .disableNestedJarScanning()
            .disableRuntimeInvisibleAnnotations()
            .rejectClasses(PluginServiceImpl::class.qualifiedName, ExtensionMapper::class.qualifiedName)
            .scan(4)

        if (plugin !is MinixImpl) plugin.log.info { "Ignore the following warning, this is expected behavior." }

        get<ExtensionMapper>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))
        get<ConfigurationMapper>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))
        get<IntegrationMapper>().processMapped(plugin, scanResult, KoinUtils.getBinds(plugin))

        scanResult.close()
    }

    private suspend fun loadDependencies(plugin: MinixPlugin) {
        logger.debug { "Loading dependencies for ${plugin.value}, with class ${plugin::class}, with loader ${plugin.classLoader.name}" }

        if (MinixApplicationBuilder.createApplication(plugin) == null) {
            logger.debug { "Plugin ${plugin.name} does not have any needed libraries." }
            return
        }
    }
}
