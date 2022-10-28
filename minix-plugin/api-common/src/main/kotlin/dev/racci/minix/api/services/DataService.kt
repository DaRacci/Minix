package dev.racci.minix.api.services

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionCompanion
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.plugin.Minix
import org.apiguardian.api.API
import kotlin.reflect.KClass

@API(status = API.Status.MAINTAINED, since = "2.3.1")
public abstract class DataService : Extension<Minix>(), StorageService<Minix> {
    public abstract fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T?

    public inline fun <reified T : MinixConfig<out MinixPlugin>> get(): T = this.getConfig(T::class)!!

    public inline fun <reified T : MinixConfig<out MinixPlugin>> getOrNull(): T? = this.getConfig(T::class)

    public inline fun <reified T : MinixConfig<out MinixPlugin>> inject(): Lazy<T> = lazy(::get)

    public companion object : ExtensionCompanion<DataService>() {
        public inline fun <reified T : MinixConfig<out MinixPlugin>> Lazy<DataService>.inject(): Lazy<T> = lazy(value::get)
    }
}
