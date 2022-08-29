@file:OptIn(MinixInternal::class)

package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.accessWith
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.api.utils.unsafeCast
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

abstract class MinixConfig<P : MinixPlugin>(
    @MinixInternal @Transient
    val primaryConfig: Boolean
) : WithPlugin<P> {

    @Transient
    final override lateinit var plugin: P; private set

    @Transient
    open val versionTransformations: Map<Int, ConfigurationTransformation> = emptyMap()

    /** Called when the config is initially loaded. */
    open fun handleLoad() = Unit

    /** Called when the config is being saved and disposed of. */
    open fun handleUnload() = Unit

    /** If overridden make sure to call super. */
    open fun load() {
        if (!this::class.doesOverride(MinixConfig<P>::plugin)) {
            val annotation = this::class.findAnnotation<MappedConfig>() ?: throw IllegalStateException("${this::class.qualifiedName} is not annotated with @MappedConfig")
            val parentKClass = annotation.parent
            val parent = getKoin().get<P>(parentKClass)
            this.plugin = parent
        }

        this.onNestedInstance<InnerConfig> {
            this.plugin = this@MinixConfig.plugin
            this.initialized = true
        }

        handleLoad()
    }

    protected inline fun <reified T : Any> onNestedInstance(action: T.() -> Unit) {
        this.onNested(this, action)
    }

    protected inline fun <reified T : Any, reified S : Any> onNested(
        baseInstance: S,
        invoke: T.() -> Unit
    ) {
        val initialNested = this.getNested<S, T>(baseInstance)
        val queue = ArrayDeque(initialNested)
        while (queue.isNotEmpty()) {
            val (instance, property) = queue.removeFirst()

            val propInstance = try {
                property.get(instance)
            } catch (e: ClassCastException) {
                continue
            }

            if (propInstance::class.qualifiedName?.startsWith(this::class.qualifiedName.orEmpty()) != true) continue

            getNested<Any, T>(propInstance).takeIf(List<*>::isNotEmpty)?.let(queue::addAll)
            propInstance::class.declaredMemberProperties
                .filterIsInstance<KProperty1<Any, T>>()
                .forEach {
                    try {
                        if (it.instanceParameter == null) return@forEach
                        it.get(propInstance).invoke()
                    } catch (e: ClassCastException) {
                        return@forEach
                    }
                }
        }
    }

    protected inline fun <reified T : Any, reified R> getNested(instance: T): List<Pair<Any, KProperty1<Any, R>>> {
        return instance::class.declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(typeOf<R>()) }
            .filterIsInstance<KProperty1<out T, R>>()
            .map { instance to it }.unsafeCast()
    }

    final override fun getKoin() = super.getKoin()

    final override fun toString(): String {
        val entries = mutableListOf<Pair<String, Any?>>()
        this.onNestedInstance<Any> {
            this::class.declaredMemberProperties
                .filterIsInstance<KProperty1<Any, *>>()
                .associateBy { it.name }
                .mapValues { runCatching { it.value.accessWith { get(this) } }.getOrNull() }
                .mapTo(entries, Map.Entry<String, Any?>::toPair)
        }

        return this::class.qualifiedName + entries.joinToString(", ", "=[", "]") { "${it.first}=${it.second}" }
    }

    // TODO: Improve performance
    @OptIn(MinixInternal::class)
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
        onNestedInstance<Any> {
            result = 31 * result + this.hashCode()
        }
        return result
    }

    interface InnerConfig {
        @MinixInternal
        var initialized: Boolean

        var plugin: MinixPlugin

        class Default : InnerConfig {
            @Transient
            override var initialized = false

            @Transient
            override lateinit var plugin: MinixPlugin
        }
    }

    @ConfigSerializable
    class Minix : InnerConfig by InnerConfig.Default() {

        @Comment("What LoggingLevel to use. Default is INFO [FATAL, ERROR, WARN, INFO, DEBUG, TRACE]")
        var loggingLevel: String = "INFO"

        fun processLoggingLevel() {
            if (!initialized) return
            if (plugin.version.isPreRelease) return

            val field = with(loggingLevel.uppercase()) { loggingLevel.takeUnless { this != it } ?: this }

            if (loggingLevel == field && plugin.log.level.name == field) {
                return
            }

            var enum = enumValues<MinixLogger.LoggingLevel>().find { it.name == field }
            if (enum == null) {
                plugin.log.warn { "Invalid logging level '$field', using '${MinixLogger.LoggingLevel.INFO.name}' instead." }
                enum = MinixLogger.LoggingLevel.INFO
            }

            plugin.log.setLevel(enum)
            loggingLevel = field
        }
    }
}
