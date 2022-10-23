package dev.racci.minix.api.utils

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

public object KoinUtils {
    @PublishedApi
    internal val reference: ConcurrentHashMap<KClass<*>, Pair<Module?, Array<KClass<*>>>> = ConcurrentHashMap<KClass<*>, Pair<Module?, Array<KClass<*>>>>()

    public inline fun <reified T : Any> getModule(instance: T): Module = reference.compute(instance::class) { _, pair ->
        val module = { binds: Array<KClass<*>> ->
            module {
                single { instance } binds binds
            }
        }

        when {
            pair == null -> {
                val binds = createBindArray(instance)
                module(binds) to binds
            }

            pair.first == null -> module(pair.second) to pair.second
            else -> pair
        }
    }!!.first!!

    public fun getBinds(instance: Any): Array<KClass<*>> = reference.computeIfAbsent(instance::class) { clazz ->
        val binds = createBindArray(instance)
        // Won't be ready until after this is called a few times.
        println("Binding ${instance::class} to ${binds.joinToString(", ") { clazz.simpleName!! }}")
        getKoin().getOrNull<MinixLogger>()?.debug { "Binding ${instance::class} to ${binds.joinToString(", ") { clazz.simpleName!! }}" }

        null to binds
    }.second

    @PublishedApi
    internal fun createBindArray(instance: Any): Array<KClass<*>> {
        val initBind = when (instance) {
            is Extension<*> -> instance::class.findAnnotation<MappedExtension>()!!.bindToKClass.takeUnless { it == Extension::class } ?: instance::class
            is MinixPlugin -> instance::class.findAnnotation<MappedPlugin>()!!.bindToKClass.takeUnless { it == MinixPlugin::class } ?: instance::class
            else -> instance::class
        }

        setOf(initBind, instance::class).toTypedArray()
        return setOf(initBind, instance::class).toTypedArray()
    }

    @MinixInternal
    public fun clearBinds(instance: Any) {
        reference.remove(instance::class)
    }
}
