package dev.racci.minix.api.services

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import kotlin.reflect.KClass

abstract class DataService : Extension<Minix>() {

    abstract fun <T : MinixConfig<out MinixPlugin>> getConfig(kClass: KClass<out T>): T?

    inline fun <reified T : MinixConfig<out MinixPlugin>> get(): T = this.getConfig(T::class)!!

    inline fun <reified T : MinixConfig<out MinixPlugin>> getOrNull(): T? = this.getConfig(T::class)

    inline fun <reified T : MinixConfig<out MinixPlugin>> inject(): Lazy<T> = lazy(::get)

    companion object : ExtensionCompanion<DataService>() {
        inline fun <reified T : MinixConfig<out MinixPlugin>> Lazy<DataService>.inject(): Lazy<T> = lazy(value::get)
    }
}
