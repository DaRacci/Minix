package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.kotlin.doesOverride
import io.papermc.paper.configuration.constraint.Constraint
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

@OptIn(MinixInternal::class)
@ConfigSerializable
abstract class MinixConfig<P : MinixPlugin> : WithPlugin<P> {

    @Transient
    @set:MinixInternal
    override lateinit var plugin: P

    @Transient
    open val version: Int = 1

    @Transient
    open val versionTransformations: Map<Int, ConfigurationTransformation> = emptyMap()

    @Constraint(ConfigConstraints.Minix::class)
    val minix: Minix = Minix()

    @ConfigSerializable
    inner class Minix : InnerConfig() {
        @Comment("What LoggingLevel to use. Default is INFO [FATAL, ERROR, WARN, INFO, DEBUG, TRACE]")
        var loggingLevel: String = "INFO"
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
        plugin = parent

        handleLoad()
    }

    abstract inner class InnerConfig {
        val plugin: P get() = this@MinixConfig.plugin
    }
}
