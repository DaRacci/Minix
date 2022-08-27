package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.kotlin.catchAndReturn
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.api.utils.unsafeCast
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

@OptIn(MinixInternal::class)
@ConfigSerializable
abstract class MinixConfig<P : MinixPlugin>(
    @MinixInternal @Transient
    val primaryConfig: Boolean
) : WithPlugin<P> {

    @Transient
    @set:MinixInternal
    override lateinit var plugin: P

    @Transient
    open val versionTransformations: Map<Int, ConfigurationTransformation> = emptyMap()

    @ConfigSerializable
    class Minix : InnerConfig() {

        @Comment("What LoggingLevel to use. Default is INFO [FATAL, ERROR, WARN, INFO, DEBUG, TRACE]")
        var loggingLevel: String = "INFO"
            set(value) {
                val upper = value.uppercase()
                if (field != upper) {
                    field = upper
                }

                val enum = enumValues<MinixLogger.LoggingLevel>().find { it.name == upper } ?: return plugin.log.warn { "Invalid logging level '$field', using '${MinixLogger.LoggingLevel.INFO.name}' instead." }
                plugin.log.setLevel(enum)
                plugin.log.info { "Set logging level to $enum" }
            }
    }

    // TODO: Find a way to make this work
    /**
     * Called when a value is updated.
     *
     * @param kPop The property that was updated.
     * @param value The previous value.
     */
    open fun updateCallback(kPop: KProperty<*>, value: Any?) = Unit

    /** Called when the config is initially loaded. */
    open fun handleLoad() = Unit

    /** Called when the config is being saved and disposed of. */
    open fun handleUnload() = Unit

    /** If overridden make sure to call super. */
    open fun load() {
        if (this::class.doesOverride(MinixConfig<P>::plugin)) {
            return log.debug { "Plugin is overriding plugin property" }
        }

        val annotation = this::class.findAnnotation<MappedConfig>() ?: throw IllegalStateException("${this::class.qualifiedName} is not annotated with @MappedConfig")
        val parentKClass = annotation.parent
        val parent = getKoin().get<P>(parentKClass)
        this.plugin = parent

        onNested<InnerConfig> {
            this.plugin = parent
        }

        handleLoad()
    }

    protected inline fun <reified T : Any> onNested(invoke: T.() -> Unit) {
        val initialNested = getNested<MinixConfig<*>, T>(this)
        val queue = ArrayDeque(initialNested)
        while (queue.isNotEmpty()) {
            val (instance, property) = queue.removeFirst()

            val propInstance = try {
                property.get(instance)
            } catch (e: ClassCastException) {
                continue
            }

            if (propInstance::class.qualifiedName?.startsWith(this::class.qualifiedName.orEmpty()) != true) continue

            getNested<Any, T>(propInstance).takeIf(List<*>::isNotEmpty)?.let { queue.addAll(it) }
            propInstance::class.declaredMemberProperties
                .onEach { log.debug { "Found property ${it.name} in ${propInstance::class.qualifiedName}" } }
                .onEach { log.debug { it } }
                .filterIsInstance<KProperty1<Any, T>>()
                .forEach {
                    try {
                        val instanceParam = it.instanceParameter ?: return@forEach
                        instanceParam.type.classifier
                        log.debug { "Invoking on ${it.name}" }
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
            .filterIsInstance<KProperty1<T, R>>()
            .map { instance to it }.unsafeCast()
    }

    override fun toString(): String {
        val entries = mutableListOf<Pair<String, Any?>>()
        this.onNested<Any> {
            this::class.declaredMemberProperties
                .filter { it.returnType.isSubtypeOf(typeOf<Any>()) }
                .filterIsInstance<KProperty1<Any, Any?>>()
                .forEach {
                    log.debug { "Adding ${it.name} to entries" }
                    it.isAccessible = true
                    entries.add(it.name to it.get(this))
                }

            this::class.declaredMemberProperties
                .filter(KProperty1<*, *>::isAccessible)
                .filterIsInstance<KProperty1<Any, *>>()
                .associateBy { it.name }
                .mapValues {
                    log.debug { "Getting value for ${it.key}" }
                    catchAndReturn<Exception, Any?> { it.value.get(this) }
                }
                .mapTo(entries) {
                    plugin.log.debug { "Adding ${it.key} with ${it.value} to entries" }
                    it.toPair()
                }
        }

        return this::class.qualifiedName + entries.joinToString(", ", "=[", "]") { "${it.first}=${it.second}" }
    }

    // TODO: Improve performance
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is MinixConfig<*>) return false

        return this::class == other::class && this.toString() == other.toString()
    }

    override fun hashCode(): Int {
        var result = plugin.hashCode()
        result = 31 * result + versionTransformations.hashCode()
        onNested<Any> {
            result = 31 * result + this.hashCode()
        }
        return result
    }

    abstract class InnerConfig {
        @Transient
        lateinit var plugin: MinixPlugin
    }
}
