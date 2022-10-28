package dev.racci.minix.core

import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.logger.PaperMinixLogger
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

internal actual class PlatformProxyImpl : PlatformProxy {
    override fun createLogger(plugin: MinixPlugin): MinixLogger {
        return PaperMinixLogger(plugin)
    }

    override fun getUUID(obj: Any): UUID {
        when (obj) {
            is Player,
            is OfflinePlayer -> (obj as OfflinePlayer).uniqueId
            else -> throw IllegalArgumentException("Cannot get UUID from $obj")
        }
    }

    internal actual fun initialize() {
        // TODO -> Annotation scan for logger converters
    }

    internal actual fun shutdown() {
    }
}
