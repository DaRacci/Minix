package dev.racci.minix.api.services

import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import kotlin.reflect.KClass

abstract class DataService : Extension<Minix>() {

    abstract fun <P : MinixPlugin, T : MinixConfig<P>> getConfig(kClass: KClass<T>): T?

    inline fun <P : MinixPlugin, reified T : MinixConfig<P>> get(): T = this.getConfig(T::class)!!

    inline fun <P : MinixPlugin, reified T : MinixConfig<P>> getOrNull(): T? = this.getConfig(T::class)

    inline fun <P : MinixPlugin, reified T : MinixConfig<P>> inject(): Lazy<T> = lazy(::get)

    companion object : ExtensionCompanion<DataService>() {
        inline fun <P : MinixPlugin, reified T : MinixConfig<P>> Lazy<DataService>.inject(): Lazy<T> = lazy(value::get)
    }
}
