package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.utils.MissingAnnotationException
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass

@OptIn(ExperimentalStdlibApi::class)
class DataServiceImpl(override val plugin: Minix) : DataService() {

    override val name = "Data Service"

    override val bindToKClass = DataService::class

    private val configClasses: LoadingCache<KClass<*>, Config> = Caffeine.newBuilder().build(::Config)

    override val configurateLoaders: LoadingCache<KClass<*>, HoconConfigurationLoader> = Caffeine.newBuilder()
        .build() {
            val config = configClasses[it]
            runBlocking { getConfigurateLoader(config.file.value) }
        }

    override val configurations: LoadingCache<KClass<*>, Any> = Caffeine.newBuilder()
        .build() { runBlocking { loadFrom(Config(it)) } }

    class Config(kClass: KClass<*>) {
        val mappedConfig: MappedConfig
        val plugin: MinixPlugin
        val file: Lazy<File>

        init {
            val annotations = kClass.annotations
            mappedConfig = annotations.filterIsInstance<MappedConfig>().firstOrNull() ?: throw MissingAnnotationException("Class ${kClass.qualifiedName} is not annotated with @MappedConfig")
            annotations.all { it !is ConfigSerializable }.ifTrue { throw MissingAnnotationException("Class ${kClass.qualifiedName} is not annotated with @ConfigSerializable") }

            if (!mappedConfig.parent.java.isAssignableFrom(MinixPlugin::class.java)) {
                throw IllegalArgumentException("Class ${mappedConfig.parent.qualifiedName} is not subclass of MinixPlugin")
            }
            plugin = getKoin().getOrNull<MinixPlugin>(mappedConfig.parent) ?: throw IllegalStateException("Could not find plugin instance for ${mappedConfig.parent}")
            file = lazy { plugin.dataFolder.resolve(mappedConfig.file) }
        }
    }

    override suspend fun getConfigurateLoader(file: File): HoconConfigurationLoader {
        return HoconConfigurationLoader.builder()
            .file(file)
            .prettyPrinting(true)
            .defaultOptions { options ->
                options.shouldCopyDefaults(true)
                    .serializers { serializerBuilder ->
                        serializerBuilder.registerAnnotatedObjects(objectMapperFactory())
                            .registerAll(ConfigurateComponentSerializer.builder().build().serializers())
                    }
            }
            .build()
    }

    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend inline fun <reified T> loadFrom(): T? = loadFrom(Config(T::class))

    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend inline fun <reified T> loadFrom(config: Config): T? = withContext(Dispatchers.IO) {
        if (!config.plugin.dataFolder.exists() && !config.plugin.dataFolder.createNewFile()) {
            config.plugin.log.warn { "Failed to create directory: ${config.plugin.dataFolder.absolutePath}" }
            return@withContext null
        }

        val loader = configurateLoaders[T::class]

        return@withContext try {
            val node = loader.load()
            val configNode = node.get<T>()

            if (!config.file.value.exists()) {
                node.set(T::class.java, config)
                loader.save(node)
            }
            configNode
        } catch (e: ConfigurateException) {
            config.plugin.log.error(e) { "Failed to load configurate file ${config.file.value.name}" }
            null
        }
    }
}
