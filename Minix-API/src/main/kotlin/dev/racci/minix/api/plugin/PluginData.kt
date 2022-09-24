package dev.racci.minix.api.plugin

import arrow.core.getOrElse
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.logger.PluginDependentMinixLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

typealias ExtensionUnit<P> = (P) -> Extension<*>

@OptIn(MinixInternal::class)
class PluginData<P : MinixPlugin>(val plugin: P) {

    @MinixInternal
    var wantsFullUnload: Boolean = false

    operator fun <T : Any?> getValue(
        thisRef: P,
        property: KProperty<*>
    ): T = this::class.memberProperties.find { it.name == property.name }?.getter?.call(thisRef).castOrThrow()

    val loader get() = getClassLoader()
    val extensionEvents by lazy { MutableSharedFlow<ExtensionStateEvent>() }

    val log by lazy { PluginDependentMinixLogger.getLogger(plugin) }

    val configurations by lazy { mutableListOf<KClass<MinixConfig<out P>>>() }

    var metrics: Metrics? = null

    override fun toString(): String {
        return "PluginData(plugin=${plugin.name}, wantsFullUnload=$wantsFullUnload, loader=$loader, extensionEvents=$extensionEvents, log=$log, configurations=$configurations, metrics=$metrics)"
    }

    private fun getClassLoader(): ClassLoader {
        return JavaPlugin::class.declaredMemberProperties
            .findKCallable("classLoader")
            .map { it.accessGet(plugin) as ClassLoader }
            .getOrElse { error("Couldn't get the classLoader property, unsupported version?") }
    }
}
