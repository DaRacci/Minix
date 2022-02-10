package dev.racci.minix.api.services

import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.utils.getKoin
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

@ApiStatus.Internal
interface PluginService {

    val loadedPlugins: MutableMap<KClass<out MinixPlugin>, MinixPlugin>

    val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>>

    operator fun <P : MinixPlugin> get(plugin: P): PluginData<P>

    fun loadPlugin(plugin: MinixPlugin)

    fun startPlugin(plugin: MinixPlugin)

    fun unloadPlugin(plugin: MinixPlugin)

    companion object : PluginService by getKoin().get()
}
