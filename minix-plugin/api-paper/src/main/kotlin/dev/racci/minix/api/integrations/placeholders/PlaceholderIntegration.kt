package dev.racci.minix.api.integrations.placeholders

import arrow.core.Ior
import arrow.core.widen
import dev.racci.minix.api.extensions.collections.get
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.toSafeString
import dev.racci.minix.integrations.Integration
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

public typealias Placeholder = Ior<() -> Any, OfflinePlayer.() -> Any>

public abstract class PlaceholderIntegration : PlaceholderExpansion(), Integration {
    private val plugin: Plugin by PluginService
    private val logger by MinixLoggerFactory
    private val placeholders = mutableMapOf<String, Placeholder>()

    /** Registers a placeholder, which doesn't require a player. */
    public fun registerPlaceholder(
        placeholder: String,
        value: () -> Any
    ): Placeholder? {
        logger.info { "Registering placeholder $placeholder" }
        return placeholders.compute(placeholder) { _, oldPlaceholder -> Ior.fromNullables(value, oldPlaceholder?.orNull()) }
    }

    /** Registers a placeholder, which a player. */
    public fun registerOfflinePlaceholder(
        placeholder: String,
        value: OfflinePlayer.() -> Any
    ): Placeholder? {
        logger.info { "Registering offline player placeholder $placeholder" }
        return placeholders.compute(placeholder) { _, oldPlaceholder -> Ior.fromNullables(oldPlaceholder?.swap()?.widen()?.orNull()?.castOrThrow(), value) }
    }

    /** Registers a placeholder, which requires an online player. */
    public fun registerOnlinePlaceholder(
        placeholder: String,
        value: Player.() -> Any
    ): Placeholder? {
        logger.info { "Registering online player placeholder $placeholder" }
        return registerOfflinePlaceholder(placeholder, value.castOrThrow())
    }

    public final override fun persist(): Boolean = true
    public final override fun getIdentifier(): String = plugin.name
    public final override fun getVersion(): String = plugin.description.version
    public final override fun getAuthor(): String = plugin.description.authors.first()

    public final override fun onPlaceholderRequest(
        player: Player?,
        params: String
    ): String? = onRequest(player, params)

    public final override fun onRequest(
        player: OfflinePlayer?,
        params: String
    ): String? {
        logger.debug { placeholders }
        logger.debug { "Placeholder request: $params" }
        val placeholder = placeholders.get(params, true).orNull() ?: return null

        logger.debug { "Placeholder found: $placeholder" }

        val result = when (player) {
            null -> this.asString(placeholder.leftOrNull()?.toSafeString())
            else -> {
                val callback = placeholder.orNull() ?: return "null"
                if (player !is Player && !callback.toString().startsWith("Player.()")) {
                    return "FUCK"
                }
                this.asString(callback.invoke(player))
            }
        }

        logger.debug { "Placeholder result: $result" }

        return result
    }

    final override suspend fun handleLoad() {
        this.register()
    }

    private fun asString(value: Any?): String = when (value) {
        is String -> value
        is Component -> LegacyComponentSerializer.legacySection().serialize(value)
        else -> value.toString()
    }
}
