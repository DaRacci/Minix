@file:OptIn(MinixInternal::class)

package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.kotlin.doesOverride
import dev.racci.minix.api.utils.unsafeCast
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

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

    protected inline fun <reified I : Any, reified R> onNested(
        baseInstance: I,
        invoke: R.() -> Unit
    ) {
        for ((inst, name, property) in this.getNested<I, R>(baseInstance)) {
            try {
                log.debug { "Invoking action on $name from class ${inst::class.simpleName}" }
                property.invoke()
            } catch (e: ClassCastException) {
                continue
            }
        }
    }

    protected inline fun <reified Instance : Any, reified Return : Any?> getNested(baseInstance: Instance): List<Triple<Any, String, Return>> {
        val baseQualifiedName = baseInstance::class.qualifiedName ?: return emptyList()
        val arrayDeque = ArrayDeque<Pair<Any, KProperty1<Any, Return>>>(getPairedInstancesOf(baseInstance, baseQualifiedName))
        val nestedProperties = mutableListOf<Triple<Any, String, Return>>()

        while (arrayDeque.isNotEmpty()) {
            val (instance, property) = arrayDeque.removeFirst()

            val propInstance = runCatching { property.get(instance) }.getOrNull() ?: continue
            if (propInstance::class.qualifiedName?.startsWith(baseQualifiedName) == true) {
                log.debug { "Found nested property ${propInstance::class.qualifiedName} in ${baseInstance::class.qualifiedName}" }
                arrayDeque.addAll(getPairedInstancesOf(propInstance, baseQualifiedName))
            }

            nestedProperties.add(Triple(instance, property.name, propInstance))
        }

        return nestedProperties
    }

    fun <R> getPairedInstancesOf(instance: Any, baseQualifiedName: String): List<Pair<Any, KProperty1<Any, R>>> {
        if (instance::class.qualifiedName?.startsWith(baseQualifiedName) != true) return emptyList()

        return instance::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>()
            .onEach { log.debug { "Found property ${it.name} of type ${it.returnType} in ${instance::class.qualifiedName}" } }
            .mapNotNull {
                log.debug { "Checking property ${it.name} of type ${it.returnType} in ${instance::class.qualifiedName}" }
                runCatching { it.get(instance) }.getOrNull()
            }.flatMap { inst -> inst::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>().map { inst to it } }
    }

//    protected tailrec inline fun <reified S : Any, reified R> getNested(
//        baseInstance: S
//    ): List<Pair<S, KProperty1<S, R>>> {
//        val qualifiedName = baseInstance::class.qualifiedName?.substringBeforeLast('.') ?: return emptyList()
//        val nested = mutableListOf<Pair<S, KProperty1<out S, R>>>()
//
//        baseInstance::class.declaredMemberProperties
//            .filterIsInstance<KProperty1<S, R>>()
//            .forEach { property ->
//                val instance = try {
//                    property.get(baseInstance)
//                } catch (e: ClassCastException) {
//                    return@forEach
//                } ?: return@forEach
//
//                if (instance::class.qualifiedName?.startsWith(qualifiedName) == true) {
//                    nested.addAll(this.`access$getNested`(instance))
//                }
//
//                nested.add(baseInstance to property)
//            }
//
//        baseInstance::class.declaredMemberProperties
//            .filterIsInstance<KProperty1<S, T>>()
//            .forEach {
//                try {
//                    if (it.instanceParameter == null) return@forEach
//                    val instance = it.get(baseInstance)
//                    if (instance::class.qualifiedName?.startsWith(baseInstance::class.qualifiedName.orEmpty()) == true) {
//                        nested.addAll(getNested(instance))
//                    }
//                    nested.add(baseInstance to it)
//                } catch (e: ClassCastException) {
//                    return@forEach
//                }
//            }
//        return nested
//    }

//    inline fun <reified T : Any, reified R> getNested(initInstance: T): MultiMap<T, KProperty1<T, R>> {
//
//        val clazz = initInstance::class
//        val properties = clazz.declaredMemberProperties
//        val tailedQueue =  ArrayDeque<Pair<T, KProperty1<out T, R>>>(properties.)
//
//        while (tailedQueue.isNotEmpty()) {
//            val (instance, property) = tailedQueue.removeFirst()
//            val rType = property.returnType
//            if (!rType.isSubtypeOf(typeOf<R>())) {
//                log.debug { "Skipping property ${property.name} because it is not a subtype of ${typeOf<R>()}" }
//                continue
//            }
//
//            if (property.instanceParameter?.type?.isSubtypeOf(typeOf<T>()) == false) {
//                log.debug { "Skipping property ${property.name} because it doesn't have a instance param of ${typeOf<T>()}" }
//                continue
//            }
//
//            tailedQueue.addAll(getNested(property.unsafeCast<KProperty1<T, R>>().get(instance), deep + 1, totalMap, tailedQueue))
//
//            tailedQueue.addLast(property.unsafeCast())
//
//            val propInstance = property.get(instance)
//            if (propInstance::class.isSubtypeOf(typeOf<R>())) {
//                totalMap.put(instance, property)
//            } else {
//                getNested(propInstance, deep + 1, totalMap, tailedQueue)
//            }
//        }
//
//        for (property in properties) {
//            val rType = property.returnType
//            if (!rType.isSubtypeOf(typeOf<R>())) {
//                log.debug { "Skipping property ${property.name} because it is not a subtype of ${typeOf<R>()}" }
//                continue
//            }
//
//            if (property.instanceParameter?.type?.isSubtypeOf(typeOf<T>()) == false) {
//                log.debug { "Skipping property ${property.name} because it doesn't have a instance param of ${typeOf<T>()}" }
//                continue
//            }
//
//            tailedQueue.addLast(property.unsafeCast())
//        }
//
//        return instance::class.declaredMemberProperties
//            .filter {
//                log.debug { "Filtering ${it.name}:${it.returnType}${it.typeParameters.joinToString(", ") { it.name } }" }
//                val rType = it.returnType.isSubtypeOf(typeOf<R>())
//                if (rType) {
//                    log.debug { "${it.name} is a subtype of ${R::class.qualifiedName}" }
//                } else log.debug { "${it.name} is not a subtype of ${R::class.qualifiedName}" }
//
//                rType
//            }
//            .filterIsInstance<KProperty1<out T, R>>()
//            .onEach { log.debug { "Got $it" } }
//            .map { instance to it }.unsafeCast()
//    }

    final override fun getKoin() = super.getKoin()

    final override fun toString(): String {
        val entries = mutableListOf<Pair<String, Any?>>()
        this::class.declaredMemberProperties.forEach {
            entries.add(it.name to it.unsafeCast<KProperty1<MinixConfig<P>, *>>().get(this))
        }

//        this.onNestedInstance<Any> {
//            val value = runCatching {
//            }
//            val clazz = runCatching { this::class }.getOrNull() ?: return@onNestedInstance
//            clazz.declaredMemberProperties
//                .filterIsInstance<KProperty1<Any, *>>()
//                .associateBy { it.name }
//                .mapValues { runCatching { it.value.accessReturn { get(this) } }.getOrNull() }
//                .mapTo(entries, Map.Entry<String, Any?>::toPair)
//        }

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

    @ConfigSerializable
    interface InnerConfig {
        @MinixInternal
        var initialized: Boolean

        var plugin: MinixPlugin

        @ConfigSerializable
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
