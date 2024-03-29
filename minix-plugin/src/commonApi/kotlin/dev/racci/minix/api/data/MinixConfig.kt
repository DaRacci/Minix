package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.Named
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.lifecycles.Lifecycle
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.data.utils.NestedUtils
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * A wrapper for creating a configuration.
 * This configuration will be automatically registered via reflection.
 *
 * To specify the output file, used the [Named] annotation.
 *
 * @param P The owning [MinixPlugin] type.
 */
public abstract class MinixConfig<P : MinixPlugin>(
    @field:Transient internal val primaryConfig: Boolean
) : WithPlugin<P>, Qualifier, Lifecycle {

    @delegate:Transient final override val plugin: P by pluginDelegate()

    @delegate:Transient final override val value: QualifierValue by Named.delegate()

    @field:Transient public open val versionTransformations: Map<Int, ConfigurationTransformation> = emptyMap()

    /** If overridden make sure to call super. */
    public open suspend fun load() {
        this.onNestedInstance<InnerConfig> {
            this.plugin = this@MinixConfig.plugin
        }

        handleLoad()
    }

    protected suspend inline fun <reified T : Any> onNestedInstance(
        crossinline action: T.() -> Unit
    ): Unit = this.onNested(this, action)

    protected suspend inline fun <reified I : Any, reified R : Any> onNested(
        baseInstance: I,
        crossinline invoke: R.() -> Unit
    ) {
        NestedUtils.getNestedInstances<R>(baseInstance::class, baseInstance)
            .collect { instance ->
                try {
                    instance.invoke()
                } catch (_: ClassCastException) {
                    return@collect
                }
            }
    }

    final override fun getKoin(): Koin = super.getKoin()

    final override fun toString(): String {
        val entries = mutableListOf<Pair<String, Any?>>()
        this::class.declaredMemberProperties.forEach {
            entries.add(it.name to it.castOrThrow<KProperty1<MinixConfig<P>, *>>().get(this))
        }

        return this::class.qualifiedName + entries.joinToString(", ", "=[", "]") { "${it.first}=${it.second}" }
    }

    // TODO: Improve performance
    final override fun equals(other: Any?): Boolean {
        if (other == null || other !is MinixConfig<*>) return false

        if (this::class != other::class) return false
        if (this.plugin !== other.plugin) return false
        if (this.primaryConfig != other.primaryConfig) return false

        return this::class == other::class && this.toString() == other.toString()
    }

    final override fun hashCode(): Int {
        var result = plugin.hashCode()
        result = 31 * result + versionTransformations.hashCode()
        runBlocking {
            onNestedInstance<Any> {
                result = 31 * result + this.hashCode()
            }
        }
        return result
    }

    @ConfigSerializable
    public interface InnerConfig {
        public val initialized: Boolean

        public var plugin: MinixPlugin

        public class Default : InnerConfig {
            // TODO -> Make sure this works
            override val initialized: Boolean get() = ::plugin.isInitialized

            @field:Transient
            override lateinit var plugin: MinixPlugin
        }
    }

    @ConfigSerializable
    public data class Minix(
        // FIXME -> @Constraint(MinixConstraints.LoggingLevel::class)
        @Comment("What LoggingLevel to use. Default is INFO [FATAL, ERROR, WARN, INFO, DEBUG, TRACE]")
        public var loggingLevel: String = "INFO",
        public val storage: Storage = Storage()
    ) : InnerConfig by InnerConfig.Default() {
        public fun processLoggingLevel() {
            if (!initialized) return

            val field = with(loggingLevel.uppercase()) { loggingLevel.takeUnless { this != it } ?: this }

            if (loggingLevel == field && plugin.logger.logLevel.name == field) {
                return
            }

            var enum = enumValues<LoggingLevel>().find { it.name == field }
            if (enum == null) {
                plugin.logger.warn { "Invalid logging level '$field', using '${LoggingLevel.INFO.name}' instead." }
                enum = LoggingLevel.INFO
            }

            plugin.logger.setLevel(enum)
            loggingLevel = field
        }

        @ConfigSerializable
        public data class Storage(
            @Comment("What storage type to use. Default is YAML [SQLITE, MARIADB]")
            val type: StorageType = StorageType.SQLITE,
            val host: String = "localhost",
            val port: Int = 3306,
            val database: String = "minix",
            val username: String = "minix",
            val password: String = "minix"
        ) : InnerConfig by InnerConfig.Default() {
            public enum class StorageType {
                SQLITE,
                MARIADB
            }
        }

        public companion object {
            public val default: Minix by lazy(::Minix)
        }
    }
}
