package dev.racci.minix.api.utils

import arrow.core.Option
import arrow.core.getOrElse
import dev.racci.minix.api.annotations.Binds
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
import org.apiguardian.api.API
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

// TODO: Possibly don't unload koin module but instead allow override.
// TODO -> Provide 'SNAPSHOT' of class after unload with warning.
public object KoinUtils {
    @PublishedApi
    internal val reference: ConcurrentHashMap<KClass<*>, Pair<Module?, Array<KClass<*>>>> = ConcurrentHashMap<KClass<*>, Pair<Module?, Array<KClass<*>>>>()

    internal inline fun <reified T : MinixPlugin> initModule(plugin: T) = module {
        single { plugin } bind T::class
        scope<T> {
            scoped { MinixLoggerFactory[plugin] } binds arrayOf(MinixLogger::class)
            scoped { plugin } binds arrayOf(T::class, MinixPlugin::class)
            plugin.scope.declare(plugin.get<CoroutineSession>(parameters = { parametersOf(plugin) }))
        }
    }

    public inline fun <reified T : Any> getModule(instance: T): Module = reference.compute(instance::class) { clazz, pair ->
        val module = { binds: Array<KClass<*>> ->
            module {
                single { instance } binds binds
            }
        }

        when {
            pair == null -> {
                val binds = createBindArray(clazz)
                module(binds) to binds
            }

            pair.first == null -> module(pair.second) to pair.second
            else -> pair
        }
    }!!.first!!

    public fun getBinds(instance: Any): Array<KClass<*>> = reference.computeIfAbsent(instance.safeCast() ?: instance::class) { clazz ->
        null to createBindArray(clazz)
    }.second

    @PublishedApi
    internal fun createBindArray(clazz: KClass<*>): Array<KClass<*>> = Option.fromNullable(clazz.findAnnotation<Binds>())
        .map(Binds::classes)
        .getOrElse(::emptyArray)
        .let { binds -> setOf(*binds, clazz::class) }
        .toTypedArray()

    @API(status = API.Status.INTERNAL)
    public fun clearBinds(instance: Any) {
        reference.remove(instance.safeCast() ?: instance::class)
    }
}
