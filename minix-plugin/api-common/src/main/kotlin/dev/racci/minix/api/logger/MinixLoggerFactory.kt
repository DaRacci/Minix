package dev.racci.minix.api.logger

import arrow.core.getOrElse
import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import org.apiguardian.api.API
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.reflect.KProperty

// TODO -> Add support for scopes, which wrap the parent logger.
@API(status = API.Status.EXPERIMENTAL, since = "5.0.0")
public object MinixLoggerFactory : KoinComponent {
    internal val EXISTING: MutableMap<MinixPlugin, MinixLogger> = mutableMapOf()

    public operator fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): MinixLogger = PluginService.fromClassloader(thisRef::class.java.classLoader)
        .map { EXISTING.getOrPut(it) { get<PlatformProxy>().createLogger(it) } }
        .getOrElse { get() }
}
