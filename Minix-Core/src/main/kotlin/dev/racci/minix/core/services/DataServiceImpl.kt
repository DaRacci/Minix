package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.serializables.Serializer
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.MissingAnnotationException
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifInitialized
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import io.leangen.geantyref.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import org.bukkit.plugin.Plugin
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.koin.core.component.KoinComponent
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.superclasses

@MappedExtension(Minix::class, "Data Service", bindToKClass = DataService::class)
class DataServiceImpl(override val plugin: Minix) : DataService() {
    private val configClasses: LoadingCache<KClass<*>, ConfigClass> = Caffeine.newBuilder().build(::ConfigClass)

    override val configurateLoaders: LoadingCache<KClass<*>, HoconConfigurationLoader> = Caffeine.newBuilder()
        .build() {
            val config = configClasses[it]
            runBlocking { getConfigurateLoader(it, config.file) }
        }

    override val configurations: LoadingCache<KClass<*>, Pair<Any, CommentedConfigurationNode>> = Caffeine.newBuilder()
        .removalListener<KClass<*>, Pair<Any, CommentedConfigurationNode>> { key, value, _ ->
            key ?: return@removalListener
            val (config, node) = value ?: return@removalListener
            log.info { "Saving and disposing configurate class ${key.simpleName}" }
            val loader = configurateLoaders[key]
            if (loader.canSave()) {
                node.set(key.java, config)
                loader.save(node)
            }
            configurateLoaders.invalidate(key)
            configClasses.invalidate(key)
        }
        .build { clazz -> runBlocking { loadFrom(ConfigClass(clazz), clazz) } }

    private val dataSource = lazy {
        HikariConfig().apply {
            jdbcUrl = "jdbc:sqlite:${plugin.dataFolder.path}/database.db"
            connectionTestQuery = "SELECT 1"
            addDataSourceProperty("cachePrepStmts", true)
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }.let(::HikariDataSource)
    }
    val database by lazy { Database.connect(dataSource.value) }

    override suspend fun handleLoad() {
        if (!plugin.dataFolder.exists() && !plugin.dataFolder.mkdirs()) {
            log.error { "Failed to create data folder!" }
        }
    }

    override suspend fun handleUnload() {
        dataSource.ifInitialized(HikariDataSource::close)
        configurations.invalidateAll()
    }

    class ConfigClass(kClass: KClass<*>) {
        val mappedConfig: MappedConfig
        val plugin: MinixPlugin
        val file: File

        init {
            val annotations = kClass.annotations
            mappedConfig = annotations.filterIsInstance<MappedConfig>().firstOrNull() ?: throw MissingAnnotationException("Class ${kClass.qualifiedName} is not annotated with @MappedConfig")
            annotations.all { it !is ConfigSerializable }.ifTrue { throw MissingAnnotationException("Class ${kClass.qualifiedName} is not annotated with @ConfigSerializable") }

            if (MinixPlugin::class !in mappedConfig.parent.superclasses) {
                throw IllegalArgumentException("Class ${mappedConfig.parent.qualifiedName} is not subclass of MinixPlugin")
            }
            plugin = getKoin().getOrNull<MinixPlugin>(mappedConfig.parent) ?: throw IllegalStateException("Could not find plugin instance for ${mappedConfig.parent}")
            file = plugin.dataFolder.resolve(mappedConfig.file)
        }
    }

    object PluginData : IdTable<String>("plugin") {

        override val id: Column<EntityID<String>> = text("name").entityId()
        var newVersion = text("new_version")
        var oldVersion = text("old_version")
    }

    class DataHolder(plugin: EntityID<String>) : Entity<String>(plugin) {

        companion object : EntityClass<String, DataHolder>(PluginData), KoinComponent {

            fun getOrNull(id: String): DataHolder? = find { PluginData.id eq id }.firstOrNull()

            fun getOrNull(plugin: Plugin): DataHolder? = getOrNull(plugin.name)

            operator fun get(plugin: Plugin): DataHolder = get(plugin.name)
        }

        var newVersion by PluginData.newVersion
        var oldVersion by PluginData.oldVersion
    }

    override suspend fun <T : Any> getConfigurateLoader(
        clazz: KClass<T>,
        file: File
    ): HoconConfigurationLoader = HoconConfigurationLoader.builder()
        .file(file)
        .prettyPrinting(true)
        .defaultOptions { options ->
            options.acceptsType(clazz.java)
            options.shouldCopyDefaults(true)
            options.serializers { serializerBuilder ->
                serializerBuilder.registerAnnotatedObjects(objectMapperFactory())
                    .registerAll(TypeSerializerCollection.defaults())
                    .registerAll(ConfigurateComponentSerializer.builder().build().serializers())
                    .registerAll(UpdateProvider.UpdateProviderSerializer.serializers)
                    .registerAll(Serializer.serializers)
                    .also { getSerializerCollection(clazz)?.let(it::registerAll) } // User defined serializers
            }
        }.build()

    private fun getSerializerCollection(clazz: KClass<*>): TypeSerializerCollection? {
        val annotation = clazz.findAnnotation<MappedConfig>()
        return if (annotation != null) {
            val extraSerializers = annotation.serializers.asList().listIterator()
            val collection = TypeSerializerCollection.builder()
            while (extraSerializers.hasNext()) {
                val nextClazz = extraSerializers.next()
                val serializer = extraSerializers.runCatching {
                    next().unsafeCast<KClass<TypeSerializer<*>>>().let {
                        it.objectInstance ?: it.createInstance()
                    }
                }.getOrNull() ?: continue
                collection.register(nextClazz.java, serializer.safeCast())
            }
            collection.build()
        } else null
    }

    @Suppress("kotlin:S6307")
    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend inline fun <reified T : Any> loadFrom(config: ConfigClass = ConfigClass(T::class)): T? = loadFrom<T>(config, T::class)?.first

    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend fun <T> loadFrom(config: ConfigClass, clazz: KClass<*>): Pair<T, CommentedConfigurationNode>? = withContext(Dispatchers.IO) {
        if (!config.plugin.dataFolder.exists() && !config.plugin.dataFolder.mkdirs()) {
            config.plugin.log.warn { "Failed to create directory: ${config.plugin.dataFolder.absolutePath}" }
            return@withContext null
        }

        val loader = configurateLoaders[clazz]

        return@withContext try {
            val node = loader.load()
            val configNode = node.get(TypeToken.get(clazz.java))
            if (!config.file.exists()) {
                node.set(clazz.java, configNode)
                loader.save(node)
            }
            configNode as T to node
        } catch (e: ConfigurateException) {
            config.plugin.log.error(e) { "Failed to load configurate file ${config.file.name}" }
            null
        }
    }

    private inline fun <reified T : Any> save(clazz: KClass<T> = T::class) {
        configurateLoaders[clazz].let { loader ->
            val (data, node) = configurations[clazz]
            node.set(clazz.java, data)
            loader.save(node)
        }
    }
}
