package dev.racci.minix.api.services

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

@ApiStatus.Internal
interface PluginService {

    val loadedPlugins: HashMap<KClass<out MinixPlugin>, MinixPlugin>

    companion object : PluginService by getKoin().get()
}
