package dev.racci.minix.core.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.github.benmanes.caffeine.cache.RemovalCause
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.api.exceptions.MissingPluginException
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.extensions.reflection.ifInitialised
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.serializables.Serializer
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.updater.providers.UpdateProvider.UpdateProviderSerializer.Companion.nonVirtualNode
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import io.papermc.paper.configuration.constraint.Constraint
import kotlinx.coroutines.runBlocking
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
import org.spongepowered.configurate.NodePath.path
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.dataClassFieldDiscoverer
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.extensions.set
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.ObjectMapper
import org.spongepowered.configurate.objectmapping.meta.NodeResolver
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import org.spongepowered.configurate.util.NamingSchemes
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@MappedExtension(Minix::class, "Data Service", bindToKClass = DataService::class)
class DataServiceImpl(override val plugin: Minix) : DataService() {
    val configDataHolder: LoadingCache<KClass<out MinixConfig<*>>, ConfigData<out MinixConfig<*>>> = Caffeine.newBuilder()
        .executor(dispatcher.get().executor)
        .removalListener<KClass<*>, ConfigData<*>> { key, value, cause ->
            if (key == null || value == null || cause == RemovalCause.REPLACED) return@removalListener
            logger.info { "Saving and disposing configurate class ${key.simpleName}." }

            value.configInstance.handleUnload()
            if (value.configLoader.canSave()) {
                value.save()
            }

            getKoin().get<PluginServiceImpl>()[plugin].configurations.remove(value.kClass.unsafeCast())
        }
        .build { key -> runBlocking(dispatcher.get()) { ConfigData(key) } }

    private val dataSource by lazy {
        HikariConfig().apply {
            jdbcUrl = "jdbc:sqlite:${plugin.dataFolder.path}/database.db"
            connectionTestQuery = "SELECT 1"
            addDataSourceProperty("cachePrepStmts", true)
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }.let(::HikariDataSource)
    }
    val database by lazy { Database.connect(dataSource) }

    override suspend fun handleLoad() {
        if (!plugin.dataFolder.exists() && !plugin.dataFolder.mkdirs()) {
            logger.error { "Failed to create data folder!" }
        }
    }

    override suspend fun handleUnload() {
        ::dataSource.ifInitialised {
            logger.info { "Closing database connection." }
            close()
        }
        dataSource::class
        configDataHolder.invalidateAll()
    }

    override fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T? = configDataHolder[kClass].configInstance as? T

    @OptIn(MinixInternal::class)
    class ConfigData<T : MinixConfig<out MinixPlugin>>(val kClass: KClass<T>) {
        val mappedConfig: MappedConfig = this.kClass.findAnnotation() ?: throw MissingAnnotationException(this.kClass, MappedConfig::class.unsafeCast())
        val configInstance: T
        val file: File
        val node: CommentedConfigurationNode
        val configLoader: HoconConfigurationLoader
//        val configClone: T

        fun save() {
            this.node.set(this.kClass, this.configInstance)
            this.configLoader.save(updateNode())
        }

        private fun updateNode(): CommentedConfigurationNode {
            if (!node.virtual()) { // we only want to migrate existing data
                val trans = createVersionBuilder()
                val startVersion = trans.version(node)

                trans.apply(node)

                val endVersion = trans.version(node)
                if (startVersion != endVersion) { // we might not have made any changes
                    configInstance.log.info { "Updated config schema from $startVersion to $endVersion" }
                }
            }

            return node
        }

        private fun createVersionBuilder(): ConfigurationTransformation.Versioned {
            val builder = ConfigurationTransformation.versionedBuilder()
            builder.versionKey("config-version")
            builder.addVersion(0, baseTransformation())
            for ((version, transformation) in this.configInstance.versionTransformations) {
                builder.addVersion(version, transformation)
            }

            return builder.build()
        }

        private fun baseTransformation(): ConfigurationTransformation {
            return ConfigurationTransformation.builder()
                .addAction(path()) { _, node ->
                    if (this.configInstance.primaryConfig) {
                        node.node("minix").set(MinixConfig.Minix())
                    }

                    null
                }
                .build()
        }

        private fun buildConfigLoader() = HoconConfigurationLoader.builder()
            .file(file)
            .prettyPrinting(true)
            .defaultOptions { options ->
                options.acceptsType(kClass.java)
                options.shouldCopyDefaults(true)
                options.serializers { serializerBuilder ->
                    serializerBuilder.registerAnnotatedObjects(objectMapper)
                        .registerAll(TypeSerializerCollection.defaults())
                        .registerAll(ConfigurateComponentSerializer.builder().build().serializers())
                        .registerAll(UpdateProvider.UpdateProviderSerializer.serializers)
                        .registerAll(Serializer.serializers)
                        .also { getSerializerCollection()?.let(it::registerAll) } // User defined serializers
                }
            }.build()

        private fun getSerializerCollection(): TypeSerializerCollection? {
            val extraSerializers = mappedConfig.serializers.asList().listIterator()
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

            return collection.build()
        }

        private fun ensureDirectory(plugin: MinixPlugin) {
            if (!plugin.dataFolder.exists() && !plugin.dataFolder.mkdirs()) {
                getKoin().get<MinixLogger>().warn { "Failed to create directory: ${plugin.dataFolder.absolutePath}" }
            }
        }

        init {
            if (!this.kClass.hasAnnotation<ConfigSerializable>()) throw MissingAnnotationException(this.kClass, ConfigSerializable::class.unsafeCast())

            val plugin = getKoin().getOrNull<MinixPlugin>(this.mappedConfig.parent) ?: throw MissingPluginException("Could not find plugin instance for ${this.mappedConfig.parent}")
            this.file = plugin.dataFolder.resolve(this.mappedConfig.file)

            this.ensureDirectory(plugin)
            this.configLoader = buildConfigLoader()

            try {
                this.node = this.configLoader.load()
                this.configInstance = this.node.get(kClass) ?: throw RuntimeException("Could not load configurate class ${this.kClass.simpleName}")
                this.node.nonVirtualNode("minix").onSuccess { node ->
                    val instance = node.get(MinixConfig.Minix::class) ?: error("The minix config section was not of the correct type!")
                    instance.plugin = plugin
                    instance.initialized = true
                    instance.processLoggingLevel()
                }

                this.configInstance.load()
//                this.configClone = this.configInstance.clone().unsafeCast()

                if (!this.file.exists()) { // || this.configInstance != this.configClone) {
                    this.save()
                }
            } catch (e: ConfigurateException) {
                getKoin().get<MinixLogger>().error(e) { "Failed to load configurate file ${this.file.name}" }
                throw e
            }

            getKoin().get<PluginServiceImpl>()[plugin].configurations.add(kClass.unsafeCast())
        }

        private companion object {
            val objectMapper: ObjectMapper.Factory = ObjectMapper.factoryBuilder()
                .addDiscoverer(dataClassFieldDiscoverer())
                .addNodeResolver { name, _ ->
                    if (name == "\$\$delegate_0") {
                        NodeResolver.SKIP_FIELD
                    } else null
                }
                .addConstraint(Constraint::class.java, Constraint.Factory())
                .defaultNamingScheme(NamingSchemes.CAMEL_CASE)
                .build()
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

    companion object : ExtensionCompanion<DataServiceImpl>()
}
