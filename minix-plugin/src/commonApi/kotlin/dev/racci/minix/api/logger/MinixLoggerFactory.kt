package dev.racci.minix.api.logger

import arrow.core.getOrElse
import com.sksamuel.aedile.core.caffeineBuilder
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.core.plugin.Minix
import kotlinx.coroutines.runBlocking
import org.apiguardian.api.API
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.minutes

// TODO -> Add support for scopes, which wrap the parent logger.
@API(status = API.Status.EXPERIMENTAL, since = "5.0.0")
public object MinixLoggerFactory : KoinComponent {
    private val loggerCache by lazy {
        caffeineBuilder<MinixPlugin, MinixLogger> {
            this.expireAfterAccess = 1.minutes
//            this.scope = get<Minix>().coroutineSession.scope
        }.build { plugin -> get(parameters = { parametersOf(plugin) }) }
    }

    public val lazy: Lazy<MinixLogger>
        get() {
            val classLoader = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass.classLoader
            return lazy { fromClassloader(classLoader) }
        }

    public operator fun get(plugin: MinixPlugin): MinixLogger = runBlocking { loggerCache.get(plugin) }

    public operator fun getValue(
        thisRef: Any,
        property: KProperty<*>?
    ): MinixLogger = fromClassloader(thisRef::class.java.classLoader)

    private fun fromClassloader(classLoader: ClassLoader): MinixLogger = get<PluginService>()
        .fromClassloader(classLoader)
        .map(::get)
        .getOrElse { this[get<Minix>()] }
}
