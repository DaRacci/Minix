package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.coroutine.registerSuspendingEvents
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.plugin.SusPlugin
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.ifNotEmpty
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import dev.racci.minix.api.utils.kotlin.invokeIfOverrides
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.coroutine.service.CoroutineSessionImpl
import kotlinx.coroutines.runBlocking
import org.bstats.bukkit.Metrics
import org.koin.dsl.bind
import kotlin.reflect.KClass

class PluginServiceImpl(val minix: Minix) : PluginService {

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

    @Suppress("UNCHECKED_CAST")
    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin] as PluginData<P>

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadModule { single { plugin } bind (plugin.bindToKClass ?: plugin::class) }
            plugin.invokeIfOverrides(SusPlugin::handleLoad.name) { plugin.handleLoad() }
        }
    }

    // Add Update checker back for Spigot as well as adding support for GitHub, mc-market and polymart
    override fun startPlugin(plugin: MinixPlugin) {
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            val cache = pluginCache[plugin]

            plugin.invokeIfOverrides(SusPlugin::handleEnable.name) {
                minix.log.debug { "Running handleEnable for ${plugin.name}" }
                plugin.handleEnable()
            }

            cache.extensions.ifNotEmpty { plugin.startInOrder() }

            cache.listeners.ifNotEmpty { collection ->
                minix.log.debug { "Registering ${collection.size} listeners for ${plugin.name}" }
                collection.forEach(plugin::registerSuspendingEvents)
            }

            plugin.bStatsId.invokeIfNotNull {
                minix.log.debug { "Registering bStats for ${plugin.name}" }
                cache.metrics = Metrics(plugin, it)
            }

            plugin.invokeIfOverrides(SusPlugin::handleAfterLoad.name) {
                minix.log.debug { "Running handleAfterLoad for ${plugin.name}" }
                plugin.handleAfterLoad()
            }

            loadedPlugins += plugin::class to plugin
        }
        coroutineService.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                minix.log.debug { "Cancelling ${it.size} tasks for ${plugin.name}" }
                it.forEach { id -> CoroutineScheduler.cancelTask(id) }
            }

            val cache = pluginCache.getIfPresent(plugin)

            cache?.loadedExtensions?.takeIf(MutableList<*>::isNotEmpty)?.let { ex ->
                minix.log.debug { "Unloading ${ex.size} extensions for ${plugin.name}" }
                plugin.shutdownInOrder()
            }

            plugin.invokeIfOverrides(SusPlugin::handleDisable.name) {
                minix.log.debug { "Running handleDisable for ${plugin.name}" }
                plugin.handleDisable()
            }

            loadedPlugins -= plugin::class
        }
    }

    private suspend inline fun <reified P : MinixPlugin> P.startInOrder() {
        val extensions = pluginCache[this].extensions.map { it.invoke(this) }.toMutableList()

        val sortedExtensions = mutableListOf<Extension<*>>()
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

        sortedExtensions.forEach { ex ->
            minix.log.debug { "Starting extension ${ex.name} for ${this.name}" }
            loadModule { single { ex } bind (ex.bindToKClass ?: ex::class) }
            ex.handleSetup()
            if (ex.loaded) pluginCache[this].loadedExtensions += ex
        }
    }

    private suspend inline fun <reified P : MinixPlugin> P.shutdownInOrder() {
        for (ex in pluginCache[this].loadedExtensions.asReversed()) {
            minix.log.debug { "Shutting down extension ${ex.name} for ${this.name}" }
            ex.invokeIfOverrides(Extension<*>::handleUnload.name) { ex.handleUnload() }
        }
    }
}
