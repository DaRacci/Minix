package dev.racci.minix.api.plugin

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.plugin.logger.PluginDependentMinixLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

typealias ExtensionUnit<P> = (P) -> Extension<*>

class PluginData<P : MinixPlugin>(val plugin: P) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any?> getValue(
        thisRef: P,
        property: KProperty<*>
    ): T = this::class.memberProperties.find { it.name == property.name }?.getter?.call(thisRef) as T

    val extensionsBuilder by lazy(plugin::ExtensionsBuilder)

    val loader by lazy { JavaPlugin::class.declaredMemberFunctions.first { it.name == "getClassLoader" }.also { it.isAccessible = true }.call(plugin) as ClassLoader }
    val extensions by lazy { mutableListOf<ExtensionUnit<P>>() }
    val loadedExtensions by lazy { mutableListOf<Extension<P>>() }
    val unloadedExtensions by lazy { mutableListOf<Extension<P>>() }
    val extensionEvents by lazy { MutableSharedFlow<ExtensionStateEvent>() }

    val log by lazy { PluginDependentMinixLogger(plugin) }

    val configurations by lazy { mutableListOf<KClass<MinixConfig<P>>>() }

    var metrics: Metrics? = null
}
