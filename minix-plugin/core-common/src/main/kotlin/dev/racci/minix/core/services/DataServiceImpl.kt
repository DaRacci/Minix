package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.github.benmanes.caffeine.cache.RemovalCause
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.events.plugin.MinixPluginStateEvent
import dev.racci.minix.api.extensions.collections.clear
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.ifInitialised
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.services.StorageService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.api.utils.collections.multiMapOf
import dev.racci.minix.core.data.ConfigData
import dev.racci.minix.core.data.PluginData
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.core.services.mapped.MapperService
import dev.racci.minix.data.extensions.nonVirtualNode
import dev.racci.minix.flowbus.subscribeFlow
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.flow.filter
import org.jetbrains.exposed.sql.Table
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.reference.WatchServiceListener
import kotlin.reflect.KClass

@MappedExtension([PluginService::class], DataService::class, threadCount = 2)
public class DataServiceImpl internal constructor() : StorageService<Minix>, MapperService<Minix>, DataService() {
    internal lateinit var watcher: WatchServiceListener
    private val configurations = multiMapOf<KClass<out MinixPlugin>, ConfigData<*, *>>()
    private val configDataHolder: LoadingCache<KClass<out MinixConfig<*>>, ConfigData<*, *>> = Caffeine.newBuilder()
        .executor(dispatcher.get().executor)
        .removalListener<KClass<*>, ConfigData<*, *>> { key, value, cause ->
            if (key == null || value == null || cause == RemovalCause.REPLACED) return@removalListener
            disposeConfig(value)
        }.build { kClass -> ConfigData(kClass, plugin) }

    override val managedTable: Table = PluginData
    override val superclass: KClass<out Any> = MinixConfig::class
    override val targetAnnotation: KClass<out Annotation> = MappedConfig::class

    override suspend fun handleLoad() {
        watcher = WatchServiceListener.builder()
            .taskExecutor(DataService.getService().dispatcher.get().executor)
            .build()

        subscribeFlow<MinixPluginStateEvent>()
            .filter { event -> event.state == MinixPluginStateEvent.State.RELOAD }
            .filter { event -> event.plugin::class in configurations.keys }
            .launchIn()
    }

    override suspend fun handleUnload() {
        ::watcher.ifInitialised(WatchServiceListener::close)
    }

    override suspend fun handlePostUnload() {
        super<StorageService>.handlePostUnload()
        this.configurations.clear { _, config -> disposeConfig(config) }
    }

    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        try {
            val kClass = classInfo.loadClass().kotlin.castOrThrow<KClass<out MinixConfig<*>>>()
            configDataHolder.put(kClass, ConfigData(kClass, plugin))
        } catch (e: NoBeanDefFoundException) {
            logger.error(e) { "Failed to register config [${plugin.value}:${classInfo.name}]" }
        } catch (e: Exception) {
            throw logger.fatal(e) { "Failed to create configuration [${plugin.value}:${classInfo.name}]" }
        }
    }

    override suspend fun forgetMapped(plugin: MinixPlugin) {
        configurations[plugin::class]?.clear {
            disposeConfig(this)
            unloadKoinModules(KoinUtils.getModule(this))
            KoinUtils.clearBinds(this)
        }
    }

    internal fun reloadConfigurations(plugin: MinixPlugin) {
        configurations[plugin::class]?.forEach { config ->
            logger.info { "Reloading configuration [${plugin.value}:${config.managedClass.simpleName}]" }
            configDataHolder.refresh(config.managedClass.castOrThrow())
        }
    }

    /** Save the configuration and remove references to it. */
    private fun disposeConfig(data: ConfigData<*, *>) {
        val instance = data.get()

        logger.info { "Saving and disposing configurate class [${instance.value}]." }
        instance.handleUnload()
        data.save()

        configurations.remove(plugin::class, data)
    }

    override fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T? = getKoin().getOrNull<T>(kClass)

    override fun getMinixConfig(plugin: MinixPlugin): MinixConfig.Minix {
        return configDataHolder.asMap().values.asSequence()
            .filter { data -> data.owner === plugin }
            .filter { data -> data.get().primaryConfig }
            .mapNotNull { data -> data.reference.node().nonVirtualNode("minix").getOrNull() }
            .map { node -> node.get(MinixConfig.Minix::class) }
            .firstOrNull() ?: MinixConfig.Minix.default // Return a default config if none are found
    }
}
