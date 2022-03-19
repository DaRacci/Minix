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
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.MissingAnnotationException
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifInitialized
import dev.racci.minix.api.utils.kotlin.ifTrue
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
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name
import kotlin.reflect.KClass

@OptIn(ExperimentalStdlibApi::class)
@MappedExtension(
    "Data Service",
    bindToKClass = DataService::class,
)
class DataServiceImpl(override val plugin: Minix) : DataService() {

    private val configClasses: LoadingCache<KClass<*>, Config> = Caffeine.newBuilder().build(::Config)

    override val configurateLoaders: LoadingCache<KClass<*>, HoconConfigurationLoader> = Caffeine.newBuilder()
        .build() {
            val config = configClasses[it]
            runBlocking { getConfigurateLoader(config.file.value) }
        }

    override val configurations: LoadingCache<KClass<*>, Any> = Caffeine.newBuilder()
        .build() { runBlocking { loadFrom(Config(it)) } }

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
        Database.connect(dataSource.value)
        val plugins = pm.plugins
        val config = get<UpdaterConfig>()
        val updateFolder = minix.dataFolder.resolve(config.updateFolder)
        transaction {
            SchemaUtils.createMissingTablesAndColumns()
            DataHolder.all().forEach { holder ->
                if (holder.id.value !in plugins.map { it.name }) {
                    val path = server.pluginsFolder.resolve(holder.loadNext.name)
                    holder.loadNext.copyTo(path.toPath())
                    pm.loadPlugin(path)
                    log.info { "Loaded ${holder.loadNext.name} from the update queue!" }
                } else {
                    val instance = plugins.first { it.name == holder.id.value }
                    pm.disablePlugin(instance)
                    log.info { "Disabled ${holder.loadNext.name} from the update queue." }
                    val path = Path(instance::class.java.protectionDomain.codeSource.location.file)
                    path.createFile()
                    updateFolder.resolve(instance.name).toPath().copyTo(path)
                    path.deleteIfExists().ifTrue { log.info { "Moved ${instance.name}'s jar file to $updateFolder to be updated." } }
                    val newPath = server.pluginsFolder.resolve(holder.loadNext.name)
                    newPath.createNewFile()
                    holder.loadNext.copyTo(newPath.toPath())
                    pm.loadPlugin(newPath)
                    log.info { "Loaded ${holder.loadNext.name} from the update queue!" }
                }
                holder.delete()
            }
        }
    }

    override suspend fun handleUnload() {
        dataSource.ifInitialized(HikariDataSource::close)
    }

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

    override suspend fun getConfigurateLoader(
        file: File
    ): HoconConfigurationLoader {
        return HoconConfigurationLoader.builder()
            .file(file)
            .prettyPrinting(true)
            .defaultOptions { options ->
                options.shouldCopyDefaults(true).serializers { serializerBuilder ->
                    serializerBuilder.registerAnnotatedObjects(objectMapperFactory())
                        .registerAll(ConfigurateComponentSerializer.builder().build().serializers())
                        .registerAll(UpdateProvider.UpdateProviderSerializer.serializers)
                }
            }
            .build()
    }

    @Suppress("kotlin:S6307")
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
