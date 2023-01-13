package dev.racci.minix.api.services

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.koin
import dev.racci.minix.core.plugin.Minix
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KClass

@ApiStatus.Experimental
@ApiStatus.AvailableSince("2.3.1")
public interface DataService<T : Table> : StorageService<Minix, T> {

    public fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T?

    /**
     * Gets the [MinixConfig.Minix] configuration section for the given [MinixPlugin].
     *
     * @param plugin The plugin to get the configuration section for.
     * @return The configuration section for the given plugin.
     */
    public suspend fun getMinixConfig(plugin: MinixPlugin): MinixConfig.Minix

    public companion object : DataService<Table> by koin.get() {
        public inline fun <reified T : MinixConfig<out MinixPlugin>> Lazy<DataService<*>>.inject(): Lazy<T> = lazy(value::get)
    }
}

public inline fun <reified T : MinixConfig<out MinixPlugin>> DataService<*>.inject(): Lazy<T> = lazy(::get)

public inline fun <reified T : MinixConfig<out MinixPlugin>> DataService<*>.getOrNull(): T? = this.getConfig(T::class)

public inline fun <reified T : MinixConfig<out MinixPlugin>> DataService<*>.get(): T = this.getConfig(T::class)!!
