package dev.racci.minix.api.utils

import arrow.core.Option
import arrow.core.getOrElse
import dev.racci.minix.api.annotations.Binds
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
import org.apiguardian.api.API
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.TypeQualifier
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

    internal fun pluginModule(plugin: MinixPlugin) = module {
        val clazz = plugin::class

        single { plugin } binds Binds.binds(clazz)
        scope(TypeQualifier(clazz)) {
            scoped { plugin } bind MinixPlugin::class
            scoped { MinixLoggerFactory[plugin] } bind MinixLogger::class
            plugin.scope.declare(plugin.get<CoroutineSession>(parameters = { parametersOf(plugin) }))
        }
    }

    public inline fun <reified T : Any> getModule(instance: T): Module = reference.compute(instance.clazz()) { clazz, pair ->
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

    public fun getBinds(instance: Any): Array<KClass<*>> = reference.computeIfAbsent(instance.clazz()) { clazz ->
        null to createBindArray(clazz)
    }.second

    @PublishedApi
    internal fun createBindArray(clazz: KClass<*>): Array<KClass<*>> = Option.fromNullable(clazz.findAnnotation<Binds>())
        .map(Binds::classes)
        .getOrElse(::emptyArray)
        .let { binds -> setOf(*binds, clazz) }
        .toTypedArray()

    @API(status = API.Status.INTERNAL)
    public fun clearBinds(instance: Any) {
        reference.remove(instance.clazz())
    }

    @PublishedApi
    internal fun Any.clazz(): KClass<*> = this as? KClass<*> ?: this::class
}
