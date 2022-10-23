package dev.racci.minix.api.plugin

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.utils.FileSystemUtils.plugin
import dev.racci.minix.api.utils.reflection.LateInitUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

public actual class PluginData {
    public actual val logger: MinixLogger by lazy { PluginDependentMinixLogger.getLogger(plugin) }

    @PublishedApi
    internal lateinit var classLoader: PluginClassLoader
    internal var wantsFullUnload: Boolean = false

    public val extensionEvents: MutableSharedFlow<ExtensionStateEvent> by lazy(::MutableSharedFlow)


    public val configurations: MutableList<*> by lazy { mutableListOf<KClass<MinixConfig<out P>>>() }

    public var metrics: Metrics? = null

    override fun toString(): String {
        return "PluginData(plugin=${plugin.name}, wantsFullUnload=$wantsFullUnload, extensionEvents=$extensionEvents, log=$log, configurations=$configurations, metrics=$metrics)"
    }

    public suspend fun getClassLoader(): PluginClassLoader = LateInitUtils.getOrSet(::classLoader) {
        JavaPlugin::class.declaredMemberProperties
            .findKCallable("classLoader")
            .fold(
                { error("Couldn't get the classLoader property, unsupported version?") },
                { prop -> prop.accessGet(plugin).castOrThrow<PluginClassLoader>() }
            )
    }
}
