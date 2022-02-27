package dev.racci.minix.api.plugin

import dev.racci.minix.api.extension.Extension
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bstats.bukkit.Metrics
import org.bukkit.event.Listener
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

typealias ExtensionUnit<P> = (P) -> Extension<*>
typealias ListenerUnit = () -> Listener

class PluginData<P : MinixPlugin>(val plugin: P) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any?> getValue(
        thisRef: P,
        property: KProperty<*>,
    ): T = this::class.memberProperties.find { it.name == property.name }?.getter?.call(thisRef) as T

    val extensionsBuilder by lazy(plugin::ExtensionsBuilder)
    val listenerBuilder by lazy(plugin::ListenerBuilder)

    val extensions by lazy { mutableListOf<ExtensionUnit<P>>() }
    val loadedExtensions by lazy { mutableListOf<Extension<out P>>() }
    val extensionEvents by lazy { MutableSharedFlow<Any>() }

    val listeners by lazy { mutableListOf<Listener>() }

    val log by lazy { MinixLogger(plugin) }

    var metrics: Metrics? = null
}
