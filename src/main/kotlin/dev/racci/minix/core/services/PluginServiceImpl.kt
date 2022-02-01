package dev.racci.minix.core.services

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import dev.racci.minix.api.coroutine.coroutine
import dev.racci.minix.api.coroutine.registerSuspendingEvents
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.SusPlugin
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.Minix
import kotlinx.coroutines.runBlocking
import org.koin.dsl.bind
import kotlin.reflect.KClass

class PluginServiceImpl(val minix: Minix) : PluginService {

    override val loadedPlugins by lazy { mutableMapOf<KClass<out MinixPlugin>, MinixPlugin>() }

    override val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>> = CacheBuilder.newBuilder()
        .build(
            CacheLoader.from { plugin: MinixPlugin ->
                PluginData(plugin)
            }
        )

    @Suppress("UNCHECKED_CAST")
    override operator fun <P : MinixPlugin> get(plugin: P): PluginData<P> = pluginCache[plugin] as PluginData<P>

    override fun loadPlugin(plugin: MinixPlugin) {
        runBlocking {
            loadModule { single { plugin } bind plugin::class }
            plugin.handleLoad()
        }
    }

    // Add Update checker back for Spigot as well as adding support for GitHub, mc-market and polymart
    // Add BStats support
    // Possibly move this complete startup method to a service utility that runs inside of Minix itself
    override fun startPlugin(plugin: MinixPlugin) {
        if (plugin::class in loadedPlugins) throw IllegalStateException("The plugin ${plugin.description.fullName} has already been registered!")
        minix.log.debug { "Handling enable for ${plugin.name} with class of ${plugin::class}" }

        coroutine.getCoroutineSession(plugin).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            val cache = pluginCache[plugin]

            if (plugin::class.doesOverride(SusPlugin::handleEnable.name)) {
                minix.log.debug { "Running handleEnable for ${plugin.name}" }
                plugin.handleEnable()
            }

            cache.extensions.takeIf(MutableList<*>::isNotEmpty)?.let { ex ->
                minix.log.debug { "Starting up ${ex.size} extensions for ${plugin.name}" }
                plugin.startInOrder()
            }

            cache.listeners.takeIf(MutableList<*>::isNotEmpty)?.let { li ->
                minix.log.debug { "Registering ${li.size} listeners for ${plugin.name} " }
                li.forEach { pm.registerSuspendingEvents(it, plugin) }
            }

            if (plugin::class.doesOverride(SusPlugin::handleAfterLoad.name)) {
                minix.log.debug { "Running handleAfterLoad for ${plugin.name}" }
                plugin.handleAfterLoad()
            }

            loadedPlugins += plugin::class to plugin
            println(loadedPlugins)
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

            if (plugin::class.doesOverride(SusPlugin::handleDisable.name)) {
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
        }
    }

    private suspend inline fun <reified P : MinixPlugin> P.shutdownInOrder() {
        for (ex in pluginCache[this].loadedExtensions.asReversed()) {
            minix.log.debug { }
            ex.handleUnload()
        }
    }
}
