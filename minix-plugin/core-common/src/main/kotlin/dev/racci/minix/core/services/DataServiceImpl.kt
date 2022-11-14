package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.github.benmanes.caffeine.cache.RemovalCause
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.ifInitialised
import dev.racci.minix.api.extensions.reflection.safeCast
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
import dev.racci.minix.flowbus.receiver.EventReceiver
import io.github.classgraph.ClassInfo
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Table
import org.koin.core.annotation.InjectedParam
import org.koin.core.context.unloadKoinModules
import org.koin.core.error.NoBeanDefFoundException
import org.spongepowered.configurate.reference.WatchServiceListener
import kotlin.reflect.KClass

@MappedExtension([PluginService::class], DataService::class, threadCount = 2)
public class DataServiceImpl internal constructor(
    @InjectedParam override val plugin: Minix
) : StorageService<Minix>, MapperService<Minix>, DataService(), EventReceiver by getKoin().get() {
    private val configurations = multiMapOf<KClass<out MinixPlugin>, ConfigData<*, *>>()

    internal lateinit var watcher: WatchServiceListener

    override val managedTable: Table = PluginData
    override val superclass: KClass<out Any> = MinixConfig::class
    override val targetAnnotation: KClass<out Annotation> = MappedConfig::class

    override suspend fun handleLoad() {
        watcher = WatchServiceListener.builder()
            .taskExecutor(DataService.getService().dispatcher.get().executor)
            .build()
    }

    override suspend fun handleReload() {
        // TODO -> Reload all configurations
    }

    override suspend fun handleUnload() {
        ::watcher.ifInitialised(WatchServiceListener::close)
    }

    override suspend fun registerMapped(
        classInfo: ClassInfo,
        plugin: MinixPlugin
    ) {
        try {
            configDataHolder[classInfo.loadClass().kotlin.castOrThrow()]
        } catch (e: NoBeanDefFoundException) {
            logger.error(e) { "Failed to register config [${plugin.value}:${classInfo.name}]" }
        } catch (e: Exception) {
            throw logger.fatal(e) { "Failed to create configuration [${plugin.value}:${classInfo.name}]" }
        }
    }

    override suspend fun forgetMapped(plugin: MinixPlugin) {
        configurations.clear { _, config ->
            disposeConfig(config)
            unloadKoinModules(KoinUtils.getModule(config))
        }

        ClassValueCtorCache.cache.configurations
            .onEach(dataService.configDataHolder::invalidate)
            .onEach { unloadKoinModules(KoinUtils.getModule(it)) }
            .forEach(KoinUtils::clearBinds)
    }

    internal val configDataHolder: LoadingCache<KClass<out MinixConfig<*>>, ConfigData<*, *>> = Caffeine.newBuilder()
        .executor(dispatcher.get().executor)
        .removalListener<KClass<*>, ConfigData<*, *>> { key, value, cause ->
            if (key == null || value == null || cause == RemovalCause.REPLACED) return@removalListener
            logger.info { "Saving and disposing configurate class [${value.configInstance.plugin}:${key.simpleName}]." }

            value.configInstance.handleUnload()
            if (value.configLoader.canSave()) {
                value.save()
            }

            configurations.remove(plugin::class, value = value.configInstance)
        }
        .build { key -> runBlocking(dispatcher.get()) { ConfigData(key) } }

    override suspend fun handlePostUnload() {
        super<StorageService>.handleUnload()
        this.configurations.clear { kClass, config ->
            val instance = getKoin().get<MinixConfig<*>>(kClass)
            instance.handleUnload()
            config.save()
        }
    }

    /** Save the configuration and remove references to it. */
    private fun disposeConfig(data: ConfigData<*, *>) {
        val instance = getKoin().get<MinixConfig<*>>(data.managedClass)

        logger.info { "Saving and disposing configurate class [${instance.value}]." }
        value.configInstance.handleUnload()
        if (value.configLoader.canSave()) {
            value.save()
        }

        configurations.remove(plugin::class, value = value.configInstance)
    }

    override fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T? = configDataHolder[kClass].configInstance.safeCast()

    override fun getMinixConfig(plugin: MinixPlugin): MinixConfig.Minix {
        for (config in configDataHolder.asMap().values) {
            if (config.configInstance.plugin !== plugin) continue
            if (!config.configInstance.primaryConfig) continue
            config.node.nonVirtualNode("minix").onSuccess { node ->
                return node.get(MinixConfig.Minix::class) ?: error("The minix config section was not of the correct type!")
            }
        }

        return MinixConfig.Minix() // Return a default config
    }
}
