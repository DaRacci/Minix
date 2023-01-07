package dev.racci.minix.api.services

import arrow.core.Option
import arrow.core.getOrElse
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.plugin.MinixPlugin
import org.apiguardian.api.API
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@API(status = API.Status.INTERNAL)
public interface PluginService {

    public val loadedPlugins: MutableMap<KClass<out MinixPlugin>, MinixPlugin>

    public fun loadPlugin(plugin: MinixPlugin)

    public fun enablePlugin(plugin: MinixPlugin)

    public fun reloadPlugin(plugin: MinixPlugin)

    public fun disablePlugin(plugin: MinixPlugin)

    public fun unloadPlugin(plugin: MinixPlugin)

    public fun firstNonMinixPlugin(): MinixPlugin?

    /** Returns the plugin instance, which is related to this classloader. */
    public fun fromClassloader(classLoader: ClassLoader): Option<MinixPlugin>

    public fun fromClass(clazz: KClass<*>): Option<MinixPlugin> = fromClassloader(clazz.java.classLoader)

    public companion object : KoinComponent {
        // TODO -> Error with class, location and property
        /** Gets the plugin that this class belongs to. */
        public operator fun <P : MinixPlugin> getValue(thisRef: Any, property: KProperty<*>?): P = get<PluginService>()
            .fromClassloader(thisRef::class.java.classLoader)
            .tap { require(it.safeCast<P>() != null) { "Plugin ${it::class.simpleName} is not of type ${property?.returnType}" } }
            .getOrElse { error("Could not find plugin for ${thisRef::class.java.name}") }.castOrThrow()
    }
}
