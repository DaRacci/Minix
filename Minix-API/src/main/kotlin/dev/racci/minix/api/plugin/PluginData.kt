package dev.racci.minix.api.plugin

import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.logger.PluginDependentMinixLogger
import dev.racci.minix.api.utils.reflection.LateInitUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@OptIn(MinixInternal::class)
class PluginData<P : MinixPlugin>(val plugin: P) {
    @PublishedApi
    internal lateinit var classLoader: PluginClassLoader

    @MinixInternal
    var wantsFullUnload: Boolean = false

    operator fun <T : Any?> getValue(
        thisRef: P,
        property: KProperty<*>
    ): T = this::class.memberProperties.find { it.name == property.name }?.getter?.call(thisRef).castOrThrow()

    val extensionEvents: MutableSharedFlow<ExtensionStateEvent> by lazy(::MutableSharedFlow)

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
