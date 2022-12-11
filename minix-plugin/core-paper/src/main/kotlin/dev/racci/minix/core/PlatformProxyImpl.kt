package dev.racci.minix.core

import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.logger.PaperMinixLogger
import dev.racci.minix.core.plugin.Minix
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.PluginClassLoader
import org.koin.core.annotation.Singleton
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Singleton([PlatformProxy::class])
internal actual class PlatformProxyImpl : PlatformProxy {
    override fun createLogger(plugin: MinixPlugin): MinixLogger {
        return PaperMinixLogger(plugin)
    }

    override fun getUUID(obj: Any): UUID {
        return when (obj) {
            is Player,
            is OfflinePlayer -> (obj as OfflinePlayer).uniqueId
            else -> throw IllegalArgumentException("Cannot get UUID from $obj")
        }
    }

    // TODO: Needs testing
    @OptIn(ExperimentalStdlibApi::class)
    actual override fun firstNonMinixPlugin(): MinixPlugin? = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
        .walk { stream ->
            stream.skip(1)
                .map { frame -> frame.declaringClass.classLoader as? PluginClassLoader }
                .filter { loader -> loader != null }
                .map { loader -> loader!!.plugin }
                .filter { plugin -> plugin is MinixPlugin }
                .filter { plugin -> plugin !is Minix }
        }.findFirst().getOrNull() as? MinixPlugin

    override fun loadDependencies(plugin: MinixPlugin) {
        MinixApplicationBuilder.createApplication(plugin)
    }

    internal actual fun initialize() {
        // TODO -> Annotation scan for logger converters
    }

    internal actual fun shutdown() {
    }
}
