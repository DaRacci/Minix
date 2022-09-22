package dev.racci.minix.api.utils

import com.github.benmanes.caffeine.cache.Caffeine
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object KoinUtils : UtilObject by UtilObject {
    private val bindCache = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build<KClass<*>, Array<KClass<*>>>()

    fun getModule(instance: Any): Module = module {
        single { instance } binds getBinds(instance)
    }

    fun getBinds(instance: Any): Array<KClass<*>> = bindCache.get(instance::class) {
        val initBind = when (instance) {
            is Extension<*> -> instance::class.findAnnotation<MappedExtension>()!!.bindToKClass.takeUnless { it == Extension::class } ?: instance::class
            is MinixPlugin -> instance::class.findAnnotation<MappedPlugin>()!!.bindToKClass.takeUnless { it == MinixPlugin::class } ?: instance::class
            else -> instance::class
        }

        setOf(initBind, instance::class).toTypedArray()
    }
}
