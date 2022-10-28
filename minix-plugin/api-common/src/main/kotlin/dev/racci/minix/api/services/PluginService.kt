package dev.racci.minix.api.services

import arrow.core.getOrElse
import arrow.core.toOption
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@API(status = API.Status.INTERNAL)
public interface PluginService {

    public val loadedPlugins: MutableMap<KClass<out MinixPlugin>, MinixPlugin>

    public val coroutineSession: LoadingCache<MinixPlugin, CoroutineSession>

    public fun loadPlugin(plugin: MinixPlugin)

    public fun enablePlugin(plugin: MinixPlugin)

    public fun reloadPlugin(plugin: MinixPlugin)

    public fun disablePlugin(plugin: MinixPlugin)

    public fun unloadPlugin(plugin: MinixPlugin)

    public fun fromClassloader(classLoader: ClassLoader): MinixPlugin?

    public fun firstNonMinixPlugin(): MinixPlugin?

    public companion object : PluginService by getKoin().get() {
        // TODO -> Error with class, location and property
        /** Gets the plugin that this class belongs to. */
        public operator fun <P : MinixPlugin> getValue(thisRef: Any, property: KProperty<*>?): P {
            return fromClassloader(thisRef::class.java.classLoader).toOption()
                .tap { require(it.safeCast<P>() != null) { "Plugin ${it::class.simpleName} is not of type ${property?.returnType}" } }
                .getOrElse { error("Could not find plugin for ${thisRef::class.java.name}") }.castOrThrow()
        }
    }
}
