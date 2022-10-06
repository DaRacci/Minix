package dev.racci.minix.api.services

import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.PluginData
import dev.racci.minix.api.utils.getKoin
import org.apiguardian.api.API
import kotlin.reflect.KClass

@API(status = API.Status.INTERNAL)
interface PluginService {

    val loadedPlugins: MutableMap<KClass<out MinixPlugin>, MinixPlugin>

    val pluginCache: LoadingCache<MinixPlugin, PluginData<MinixPlugin>>

    val coroutineSession: LoadingCache<MinixPlugin, CoroutineSession>

    operator fun <P : MinixPlugin> get(plugin: P): PluginData<P>

    fun loadPlugin(plugin: MinixPlugin)

    fun startPlugin(plugin: MinixPlugin)

    fun unloadPlugin(plugin: MinixPlugin)

    fun reloadPlugin(plugin: MinixPlugin)

    suspend fun fromClassloader(classLoader: ClassLoader): MinixPlugin?

    fun firstNonMinixPlugin(): MinixPlugin?

    companion object : PluginService by getKoin().get()
}
