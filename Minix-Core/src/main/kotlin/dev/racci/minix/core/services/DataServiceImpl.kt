package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.data.UpdaterConfig
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.serializables.Serializer
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.MissingAnnotationException
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifFalse
import dev.racci.minix.api.utils.kotlin.ifInitialized
import dev.racci.minix.api.utils.kotlin.ifTrue
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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

@MappedExtension("Data Service", bindToKClass = DataService::class)
class DataServiceImpl(override val plugin: Minix) : DataService() {
    private val config by inject<UpdaterConfig>()
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

    // TODO: If this method is jank look into using the update folder from bukkit, note that the plugin must have the same name as the currently loaded one in that case.
    override suspend fun handleLoad() {
        if (!plugin.dataFolder.exists() && !plugin.dataFolder.mkdirs()) {
            log.error { "Failed to create data folder!" }
        }

        Database.connect(dataSource.value)
        val plugins = pm.plugins
        val updateFolder = minix.dataFolder.resolve(config.updateFolder)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(DataHolder.table)
            DataHolder.all().forEach { holder ->
                val func = {
                    val path = server.pluginsFolder.resolve(holder.loadNext.name)
                    log.debug { "Moving ${holder.loadNext} to path ${path.name}." }
                    holder.loadNext.copyTo(path.toPath())
                    log.info { "Moved ${holder.id.value}'s new jar file to the plugins folder!." }
                    path
                }
                when (holder.id.value) {
                    "Minix" -> func()
                    !in plugins.map { it.name } -> {
                        func().also(pm::loadPlugin)
                        log.info { "Loaded ${holder.loadNext.name} from the update queue!" }
                    }
                    else -> {
                        val instance = plugins.first { it.name == holder.id.value }.also(pm::disablePlugin)
                        log.info { "Disabled ${holder.loadNext.name} from the update queue." }
                        val path = Path(instance::class.java.protectionDomain.codeSource.location.file)
                        updateFolder.resolve("old-versions").also { folder ->
                            folder.mkdirs().ifFalse { return@forEach log.error { "Failed to create old-versions folder!" } }
                            folder.resolve(path.name).also { file ->
                                file.createNewFile().ifFalse { return@forEach log.error { "Failed to create old-versions/${path.name}!" } }
                                path.copyTo(file.toPath())
                                path.deleteIfExists()
                                log.debug { "Moved ${path.name} to old-versions/${file.name}." }
                            }
                        }
                        func().also(pm::loadPlugin)
                        log.info { "Loaded ${holder.loadNext.name} from the update queue!" }
                    }
                }
                holder.delete()
            }
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
        var loadNext = text("path")
    }

    class DataHolder(plugin: EntityID<String>) : Entity<String>(plugin) {

        companion object : EntityClass<String, DataHolder>(PluginData), KoinComponent {

            fun getOrNull(id: String): DataHolder? = find { PluginData.id eq id }.firstOrNull()

            fun getOrNull(plugin: Plugin): DataHolder? = getOrNull(plugin.name)

            operator fun get(plugin: Plugin): DataHolder = get(plugin.name)
        }

        private var _loadNext: String by PluginData.loadNext

        var loadNext: Path
            get() = Path(_loadNext)
            set(value) { _loadNext = value.toString() }
    }

    override suspend fun <T : Any> getConfigurateLoader(
        clazz: KClass<T>,
        file: File
    ): HoconConfigurationLoader {
        return HoconConfigurationLoader.builder()
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
                }
            }
            .build()
    }

    @Suppress("kotlin:S6307")
    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend inline fun <reified T : Any> loadFrom(): T? = loadFrom(ConfigClass(T::class))

    @Suppress("kotlin:S6307")
    @Throws(IOException::class, MissingAnnotationException::class, IllegalArgumentException::class) // uwu dangerous
    suspend inline fun <reified T : Any> loadFrom(config: ConfigClass): T? = loadFrom<T>(config, T::class)?.first

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
