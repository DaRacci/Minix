package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.RemovalCause
import com.sksamuel.aedile.core.caffeineBuilder
import dev.racci.minix.api.annotations.Binds
import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.annotations.Threads
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.events.plugin.MinixPluginStateEvent
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.ifInitialised
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.core.data.ConfigData
import dev.racci.minix.core.data.PluginData
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.mapped.MapperService
import dev.racci.minix.data.extensions.nonVirtualNode
import dev.racci.minix.flowbus.subscribeFlow
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.module.Module
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.reference.WatchServiceListener
import kotlin.reflect.KClass

@Threads(1)
@Binds([DataService::class])
@Depends([PluginService::class])
public class DataServiceImpl internal constructor() : MapperService<Minix, MinixConfig<*>>(), DataService<PluginData> {
    internal lateinit var watcher: WatchServiceListener

    private val configHolder = caffeineBuilder<KClass<out MinixConfig<*>>, ConfigData<*, *>> {
        dispatcher = this@DataServiceImpl.dispatcher.get()
        evictionListener = label@{ key, value, cause ->
            if (key == null || value == null || cause == RemovalCause.REPLACED) return@label
            disposeConfig(value)
        }
    }.build { kClass -> ConfigData(kClass, plugin) }

    override suspend fun handleLoad() {
        watcher = WatchServiceListener.builder()
            // .taskExecutor(DataService.dispatcher.get().executor)
            .build()

        subscribeFlow<MinixPluginStateEvent>()
            .filter { event -> event.state == MinixPluginStateEvent.State.RELOAD }
            .conflate()
            .onEach { event -> reloadConfigurations(event.plugin) }
            .launchIn()
    }

    override suspend fun handleUnload() {
        ::watcher.ifInitialised(WatchServiceListener::close)
    }

    override suspend fun handlePostUnload() {
        super<DataService>.handlePostUnload()
        this.configHolder.invalidateAll()
    }

    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin,
        module: Module
    ) {
        try {
            val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<out MinixConfig<*>>>()
            configHolder.put(kClass, ConfigData(kClass, plugin))
        } catch (e: NoBeanDefFoundException) {
            logger.error(e) { "Failed to register config [${plugin.value}:${classInfo.name}]" }
        } catch (e: Exception) {
            throw logger.fatal(e) { "Failed to create configuration [${plugin.value}:${classInfo.name}]" }
        }
    }

    override suspend fun forgetMapped(plugin: MinixPlugin) {
        filtered(plugin).forEach { value ->
            configHolder.invalidate(value.managedClass.castOrThrow())
            unloadKoinModules(KoinUtils.getModule(value.managedClass))
            KoinUtils.clearBinds(value.managedClass)
        }
    }

    // TODO: Better way of doing this?
    internal suspend fun reloadConfigurations(plugin: MinixPlugin) {
        configHolder.asDeferredMap().entries
            .filter { it.key.java.classLoader === plugin::class.java.classLoader }
            .map { it.value.await() }
            .forEach { config ->
                logger.info { "Reloading configuration [${plugin.value}:${config.managedClass.simpleName}]" }
                configHolder.invalidate(config.managedClass.castOrThrow())
                configHolder.get(config.managedClass.castOrThrow())
            }
    }

    /** Save the configuration and remove references to it. */
    private suspend fun disposeConfig(data: ConfigData<*, *>) {
        val instance = data.get()

        logger.info { "Saving and disposing configurate class [${instance.value}]." }
        instance.handleUnload()
        data.save()
    }

    override fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T? = getKoin().getOrNull<T>(kClass)

    override suspend fun getMinixConfig(plugin: MinixPlugin): MinixConfig.Minix = filtered(plugin)
        .first { it.get().primaryConfig }.reference.node().nonVirtualNode("minix").getOrNull()?.get(MinixConfig.Minix::class)
        ?: MinixConfig.Minix.default

    private fun filtered(plugin: MinixPlugin): Sequence<ConfigData<*, *>> = configHolder.asDeferredMap().entries
        .filter { it.key.java.classLoader === plugin::class.java.classLoader }
        .asSequence()
        .map { runBlocking { it.value.await() } }
}
