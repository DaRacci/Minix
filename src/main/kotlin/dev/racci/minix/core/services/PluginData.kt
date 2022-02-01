package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bukkit.event.Listener
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

typealias ExtensionUnit<P> = (P) -> Extension<*>
typealias ListenerUnit = () -> Listener

class PluginData<P : MinixPlugin>(val plugin: P) {

    operator fun <T : Any?> getValue(
        thisRef: P,
        property: KProperty<*>,
    ): T {
        return this::class.memberProperties.find { it.name == property.name }?.getter?.call(thisRef) as T
    }

    val extensionsBuilder by lazy(plugin::ExtensionsBuilder)
    val listenerBuilder by lazy(plugin::ListenerBuilder)

    val extensions by lazy { mutableListOf<ExtensionUnit<P>>() }
    val loadedExtensions by lazy { mutableListOf<Extension<P>>() }
    val extensionEvents by lazy { MutableSharedFlow<Any>() }

    val listeners by lazy { mutableListOf<Listener>() }

    val log by lazy { MinixLogger(plugin) }
}
