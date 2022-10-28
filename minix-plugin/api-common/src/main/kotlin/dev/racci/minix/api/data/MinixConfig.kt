package dev.racci.minix.api.data

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.data.utils.NestedUtils
import kotlinx.coroutines.runBlocking
import org.apiguardian.api.API
import org.koin.core.Koin
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

public abstract class MinixConfig<P : MinixPlugin>(
    @field:Transient
    @API(status = API.Status.INTERNAL)
    public val primaryConfig: Boolean
) : WithPlugin<P> {

    @field:Transient
    final override lateinit var plugin: P; private set

    @field:Transient
    public open val versionTransformations: Map<Int, ConfigurationTransformation> = emptyMap()

    /** Called when the config is initially loaded. */
    public open fun handleLoad(): Unit = Unit

    /** Called when the config is being saved and disposed of. */
    public open fun handleUnload(): Unit = Unit

    /** If overridden make sure to call super. */
    public open suspend fun load() {
        if (!this::class.doesOverride(MinixConfig<P>::plugin)) {
//            val annotation = this::class.findAnnotation<MappedConfig>() ?: throw IllegalStateException("${this::class.qualifiedName} is not annotated with @MappedConfig")
            val parent = PluginService.fromClassloader(this::class.java.classLoader) ?: error("Could not find plugin from classloader ${this::class.java.classLoader}")
            logger.debug { "Loading config for ${this::class.qualifiedName} with parent ${parent::class.qualifiedName}" }
            this.plugin = plugin
        }

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
                } catch (_: ClassCastException) { return@collect }
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
            override val initialized: Boolean by ::plugin::isInitialized

            @Transient
            override lateinit var plugin: MinixPlugin
        }
    }

    @ConfigSerializable
    public class Minix : InnerConfig by InnerConfig.Default() {

        // TODO -> Fix constraint
        /*@Constraint(MinixConstraints.LoggingLevel::class)*/
        @Comment("What LoggingLevel to use. Default is INFO [FATAL, ERROR, WARN, INFO, DEBUG, TRACE]")
        public var loggingLevel: String = "INFO"

        public fun processLoggingLevel() {
            if (!initialized) return

            val field = with(loggingLevel.uppercase()) { loggingLevel.takeUnless { this != it } ?: this }

            if (loggingLevel == field && plugin.log.logLevel.name == field) {
                return
            }

            var enum = enumValues<LoggingLevel>().find { it.name == field }
            if (enum == null) {
                plugin.log.warn { "Invalid logging level '$field', using '${LoggingLevel.INFO.name}' instead." }
                enum = LoggingLevel.INFO
            }

            plugin.log.setLevel(enum)
            loggingLevel = field
        }
    }
}
