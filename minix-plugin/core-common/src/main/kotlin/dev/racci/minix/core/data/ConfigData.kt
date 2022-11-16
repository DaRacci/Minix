package dev.racci.minix.core.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.exceptions.MinixConfigException
import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.api.exceptions.MissingPluginException
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.services.DataServiceImpl
import dev.racci.minix.data.extensions.nonVirtualNode
import dev.racci.minix.data.serializers.kotlin.Serializer
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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
import org.spongepowered.configurate.reference.ConfigurationReference
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import org.spongepowered.configurate.util.NamingSchemes
import java.io.File
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * Wrapper around [MinixActualConfig]
 *
 * @param P The owning plugin type.
 * @param C The config type.
 * @property managedClass The class of this configuration.
 * @property owner The owning plugin.
 * @param loader The replacement loader.
 */
public class ConfigData<P, C>
@Throws(
    MissingAnnotationException::class,
    MissingPluginException::class,
    MinixConfigException::class,
    ConfigurateException::class
)
internal constructor(
    internal val managedClass: KClass<in C>,
    internal val owner: P,
    loader: HoconConfigurationLoader? = null
) : KoinComponent where P : MinixPlugin, C : MinixConfig<P> {
    public val reference: ConfigurationReference<CommentedConfigurationNode>

    public fun get(): C = getKoin().get(managedClass)

    public fun save() {
        this.ensureDirectory()
        this.reference.node().set(this.managedClass, getKoin().get(this.managedClass))
        this.reference.loader().save(updateNode())
    }

    private fun updateNode(): CommentedConfigurationNode {
        val node = this.reference.node()
        if (!node.virtual()) { // we only want to migrate existing data
            val trans = createVersionBuilder()
            val startVersion = trans.version(node)

            trans.apply(node)

            val endVersion = trans.version(node)
            if (startVersion != endVersion) { // we might not have made any changes
                this.owner.logger.info { "Updated config schema from $startVersion to $endVersion" }
            }
        }

        return node
    }

    private fun createVersionBuilder(): ConfigurationTransformation.Versioned {
        val builder = ConfigurationTransformation.versionedBuilder()
        builder.versionKey("config-version")
        builder.addVersion(0, baseTransformation())
        for ((version, transformation) in getKoin().get<C>(this.managedClass).versionTransformations) {
            builder.addVersion(version, transformation)
        }

        return builder.build()
    }

    private fun baseTransformation(): ConfigurationTransformation {
        return ConfigurationTransformation.builder()
            .addAction(path()) { _, node ->
                if (getKoin().get<C>(this.managedClass).primaryConfig) {
                    node.node("minix").set(MinixConfig.Minix())
                }

                null
            }
            .build()
    }

    private fun ensureDirectory() {
        if (!this.owner.dataFolder.exists() && !this.owner.dataFolder.toFile().mkdirs()) {
            getKoin().get<MinixLogger>().warn { "Failed to create directory: ${this.owner.dataFolder.absolutePathString()}" }
        }
    }

    init {
        if (!this.managedClass.hasAnnotation<ConfigSerializable>()) throw MissingAnnotationException(this.managedClass, ConfigSerializable::class)
        val annotation = this.managedClass.findAnnotation<MappedConfig>() ?: throw MissingAnnotationException(this.managedClass, MappedConfig::class)

        val file = this.owner.dataFolder.resolve(annotation.file)
        val serializers = annotation.serializers.filterIsInstance<KClass<out TypeSerializer<*>>>()

        this.owner.logger.debug { "Loading config: ${this.managedClass.simpleName} from file: ${file.absolutePathString()}" }
        if (serializers.isNotEmpty()) this.owner.logger.debug { "Additional serializers: ${serializers.joinToString { it.simpleName ?: "Unknown" }}" }

        this.reference = ConfigurationReference.watching(
            { loader ?: buildConfigLoader(file.toFile(), this.managedClass) },
            file,
            get<DataServiceImpl>().watcher
        )

        this.reference.errors().cache().subscribe { (phase, err) ->
            this.owner.logger.error(err) { "Error during config phase: $phase" }
        }

        // TODO -> Log Difference
        this.reference.updates().cache().subscribe { node ->
            this.owner.logger.debug { "Received updated: ${this.managedClass.simpleName}" }

            loadModule {
                single { node.get(managedClass) }
            }
        }

        runCatching {
            this.reference.load()

            this.reference.node().nonVirtualNode("minix").onSuccess { node ->
                val instance = node.get(MinixConfig.Minix::class) ?: error("The minix config section was not of the correct type.")
                instance.plugin = this.owner
                instance.processLoggingLevel()
            }

            if (!file.exists()) { // Save on first load if the file doesn't exist
                this.save()
            }
        }.onFailure { err ->
            throw if (err is ConfigurateException) {
                this.owner.logger.fatal { "Failed to load config: ${this.managedClass.simpleName} from file: ${file.absolutePathString()}" }
                err
            } else MinixConfigException(err)
        }
    }

    internal companion object {
        val objectMapper: ObjectMapper.Factory = ObjectMapper.factoryBuilder()
            .addDiscoverer(dataClassFieldDiscoverer())
            .addNodeResolver { name, _ ->
                if (name == "\$\$delegate_0") {
                    NodeResolver.SKIP_FIELD
                } else null
            }
//            .addConstraint(Constraint::class.java, Constraint.Factory())
            .defaultNamingScheme(NamingSchemes.CAMEL_CASE)
            .build()
    }
}

private fun getSerializerCollection(
    annotation: MappedConfig
): TypeSerializerCollection? {
    val extraSerializers = annotation.serializers.asList().listIterator()
    val collection = TypeSerializerCollection.builder()
    while (extraSerializers.hasNext()) {
        val nextClazz = extraSerializers.next()
        val serializer = extraSerializers.runCatching {
            next().castOrThrow<KClass<TypeSerializer<*>>>().let {
                it.objectInstance ?: it.createInstance()
            }
        }.getOrNull() ?: continue
        collection.register(nextClazz.java, serializer.safeCast())
    }

    return collection.build()
}

public fun buildConfigLoader(
    file: File,
    clazz: KClass<*>
): HoconConfigurationLoader = HoconConfigurationLoader.builder()
    .file(file)
    .prettyPrinting(true)
    .defaultOptions { options ->
        options.acceptsType(clazz.java)
        options.shouldCopyDefaults(true)
        options.serializers { serializerBuilder ->
            serializerBuilder.registerAnnotatedObjects(ConfigData.objectMapper)
                .registerAll(TypeSerializerCollection.defaults())
                .registerAll(ConfigurateComponentSerializer.builder().build().serializers())
                .registerAll(Serializer.serializers)
                .also { getSerializerCollection(clazz.findAnnotation()!!)?.let(it::registerAll) } // User defined serializers
        }
    }.build()
