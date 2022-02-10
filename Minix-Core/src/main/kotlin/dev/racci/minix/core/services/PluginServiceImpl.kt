package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.coroutine.coroutine
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
import kotlinx.coroutines.runBlocking
import org.bstats.bukkit.Metrics
import org.koin.dsl.bind
import kotlin.reflect.KClass

class PluginServiceImpl(val minix: Minix) : PluginService {

    override val loadedPlugins by lazy { mutableMapOf<KClass<out MinixPlugin>, MinixPlugin>() }

    override val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>> = Caffeine.newBuilder().build { plugin: MinixPlugin -> PluginData(plugin) }

    @Suppress("UNCHECKED_CAST")
    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin] as PluginData<P>

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadModule { single { plugin } bind (plugin.bindToKClass ?: plugin::class) }
            plugin.invokeIfOverrides(SusPlugin::handleLoad.name) { plugin.handleLoad() }
        }
    }

    // Add Update checker back for Spigot as well as adding support for GitHub, mc-market and polymart
    // Add BStats support
    override fun startPlugin(plugin: MinixPlugin) {
        if (plugin::class in loadedPlugins) throw IllegalStateException("The plugin ${plugin.description.fullName} has already been registered!")
        minix.log.debug { "Handling enable for ${plugin.name} with class of ${plugin::class}" }

        coroutine.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
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
        coroutine.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    override fun unloadPlugin(plugin: MinixPlugin) {
        runBlocking {
            CoroutineScheduler.activateTasks(plugin)?.takeIf(IntArray::isNotEmpty)?.let {
                minix.log.debug { "Cancelling ${it.size} tasks for ${plugin.name}" }
                it.forEach { id -> CoroutineScheduler.cancelTask(id) }
            }

            val cache = pluginCache.getIfPresent(plugin)

            plugin.invokeIfOverrides(SusPlugin::handleDisable.name) {
                minix.log.debug { "Running handleDisable for ${plugin.name}" }
                plugin.handleDisable()
            }

            cache?.loadedExtensions?.takeIf(MutableList<*>::isNotEmpty)?.let { ex ->
                minix.log.debug { "Unloading ${ex.size} extensions for ${plugin.name}" }
                plugin.shutdownInOrder()
            }

            loadedPlugins -= plugin::class
        }
    }

    private suspend inline fun <reified P : MinixPlugin> P.startInOrder() {
        val sorted = mutableListOf<Extension<*>>()
        val visited = mutableSetOf<KClass<out Extension<*>>>()
        val toVisit = pluginCache[this].extensions.map { it.invoke(this) }.toMutableList()
        val toVisitClasses = toVisit.map { it::class }.toMutableSet()
        while (toVisit.isNotEmpty()) {
            val next = toVisit.first()
            toVisit.remove(next)
            if (next::class in visited) continue
            visited += next::class
            next.dependencies.forEach { kClass ->
                if (kClass in toVisitClasses) {
                    toVisitClasses -= kClass
                    toVisit.find { it::class == kClass }?.let { toVisit.remove(it) }
                }
            }
            sorted += next
        }
        sorted.forEach { ex ->
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
