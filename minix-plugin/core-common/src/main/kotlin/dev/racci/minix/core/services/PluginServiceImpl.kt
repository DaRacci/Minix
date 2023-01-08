package dev.racci.minix.core.services

import arrow.core.Option
import arrow.core.firstOrNone
import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.annotations.Binds
import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.annotations.Required
import dev.racci.minix.api.autoscanner.Scanner
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.reflection.OverrideUtils
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.mapped.ExtensionMapper
import dev.racci.minix.core.services.mapped.IntegrationMapper
import io.github.toolfactory.jvm.Driver
import kotlinx.coroutines.runBlocking
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass
import kotlin.reflect.KSuspendFunction0

@Required
@Binds([PluginService::class])
@Depends([ExtensionMapper::class, DataService::class, IntegrationMapper::class]) // TODO: Do we need these?
public class PluginServiceImpl internal constructor() : PluginService, Extension<Minix>() {
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

            if (plugin !is Minix) {
                PlatformProxy.loadDependencies(plugin)
                loadKoinModules(KoinUtils.getModule(plugin))
            }

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

    override fun reloadPlugin(plugin: MinixPlugin): Unit = runBlocking {
        get<DataServiceImpl>().reloadConfigurations(plugin)
    }

    override fun disablePlugin(plugin: MinixPlugin) {
        runBlocking {
            logRunning(plugin, plugin::handleDisable)
            val dataService = get<DataServiceImpl>()
            val extensionMapper = get<ExtensionMapper>()

            dataService.forgetMapped(plugin)
            extensionMapper.disableExtensions(plugin)

            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                logger.trace { "Cancelling ${it.size} tasks." }
                it.forEach { id -> CoroutineScheduler.shutdownTask(id) }
            }
        }
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            logRunning(plugin, plugin::handleUnload)

            get<ExtensionMapper>().unloadExtensions(plugin)

            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                logger.trace { "Cancelling ${it.size} tasks." }
                it.forEach { id -> CoroutineScheduler.shutdownTask(id) }
            }

            loadedPlugins -= plugin::class
        }
    }

    override fun firstNonMinixPlugin(): MinixPlugin? = PlatformProxy.firstNonMinixPlugin()

    override fun fromClassloader(classLoader: ClassLoader): Option<MinixPlugin> = loadedPlugins.values.firstOrNone { plugin ->
        plugin.platformClassLoader === classLoader
    }

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
        // rejectClasses(PluginServiceImpl::class.qualifiedName, ExtensionMapper::class.qualifiedName)
        if (plugin !is Minix) plugin.logger.info { "Ignore the following warning, this is expected behavior." }
        val scanner = Scanner.ofJar(plugin::class.java.protectionDomain.codeSource.location.path.substringAfterLast('/'))

        get<ExtensionMapper>().processMapped(plugin, scanner)
        get<DataServiceImpl>().processMapped(plugin, scanner)
        get<IntegrationMapper>().processMapped(plugin, scanner)
    }
}
