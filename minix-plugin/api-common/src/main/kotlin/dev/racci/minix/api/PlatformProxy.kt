package dev.racci.minix.api

import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import org.apiguardian.api.API
import java.util.UUID

@API(status = API.Status.INTERNAL)
public interface PlatformProxy {
    public fun createLogger(plugin: MinixPlugin): MinixLogger

    public fun firstNonMinixPlugin(): MinixPlugin?

    /** Get the UUID of a platforms player object. */
    public fun getUUID(obj: Any): UUID

    public fun loadDependencies(plugin: MinixPlugin)

    public companion object : PlatformProxy by getKoin().get()
}
