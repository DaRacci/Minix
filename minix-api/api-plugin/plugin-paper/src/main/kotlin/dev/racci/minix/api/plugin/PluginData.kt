package dev.racci.minix.api.plugin

import kotlinx.coroutines.flow.MutableSharedFlow
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

public actual class PluginData {

    @PublishedApi
    internal lateinit var classLoader: PluginClassLoader
    internal var wantsFullUnload: Boolean = false

    public val extensionEvents: MutableSharedFlow<ExtensionStateEvent> by lazy(::MutableSharedFlow)

    val log by lazy { PluginDependentMinixLogger.getLogger(plugin) }

    val configurations by lazy { mutableListOf<KClass<MinixConfig<out P>>>() }

    var metrics: Metrics? = null

    override fun toString(): String {
        return "PluginData(plugin=${plugin.name}, wantsFullUnload=$wantsFullUnload, extensionEvents=$extensionEvents, log=$log, configurations=$configurations, metrics=$metrics)"
    }

    suspend fun getClassLoader(): PluginClassLoader = LateInitUtils.getOrSet(::classLoader) {
        JavaPlugin::class.declaredMemberProperties
            .findKCallable("classLoader")
            .fold(
                { error("Couldn't get the classLoader property, unsupported version?") },
                { prop -> prop.accessGet(plugin).castOrThrow<PluginClassLoader>() }
            )
    }
}