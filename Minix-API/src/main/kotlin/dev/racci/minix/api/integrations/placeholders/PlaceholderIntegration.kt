package dev.racci.minix.api.integrations.placeholders

import dev.racci.minix.api.extensions.collections.get
import dev.racci.minix.api.integrations.Integration
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

abstract class PlaceholderIntegration : PlaceholderExpansion(), Integration {
    private val placeholders = mutableMapOf<String, Triple<(() -> Any)?, (Player.() -> Any)?, (OfflinePlayer.() -> Any)?>>()

    /** Registers a placeholder, which doesn't require a player. */
    fun registerPlaceholder(
        placeholder: String,
        value: () -> Any
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(value, oldTriple?.second, oldTriple?.third) }

    /** Registers a placeholder, which requires an online player. */
    fun registerOnlinePlaceholder(
        placeholder: String,
        value: Player.() -> Any
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(oldTriple?.first, value, oldTriple?.third) }

    /** Registers a placeholder, which a player. */
    fun registerOfflinePlaceholder(
        placeholder: String,
        value: OfflinePlayer.() -> Any
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(oldTriple?.first, oldTriple?.second, value) }

    final override fun persist(): Boolean = true
    final override fun getIdentifier(): String = plugin.name
    final override fun getVersion(): String = plugin.description.version
    final override fun getAuthor(): String = plugin.description.authors.first()

    final override fun onPlaceholderRequest(
        player: Player?,
        params: String
    ): String? = onRequest(player, params)

    final override fun onRequest(
        player: OfflinePlayer?,
        params: String
    ): String? {
        logger.debug { placeholders }
        logger.debug { "Placeholder request: $params" }
        val placeholder = placeholders.get(params, true).orNull() ?: return null

        val result = when (player) {
            null -> this.asString(placeholder.first)
            is Player -> this.asString(placeholder.second?.invoke(player))
            else -> this.asString(placeholder.third?.invoke(player))
        }

        logger.debug { "Placeholder result: $result" }

        return result
    }

    final override suspend fun handleLoad() {
        this.register()
    }

    private fun asString(value: Any?): String? = when (value) {
        is String -> value
        is Component -> LegacyComponentSerializer.legacySection().serialize(value)
        null -> null
        else -> {
            warning("Placeholder value is not a string or component: ${value::class.qualifiedName}")
            null
        }
    }
}
