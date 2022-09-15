package dev.racci.minix.api.integrations.placeholders

import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.utils.collections.CollectionUtils.getIgnoreCase
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
    final override fun getIdentifier(): String = pluginRegister.name
    final override fun getVersion(): String = pluginRegister.description.version
    final override fun getAuthor(): String = pluginRegister.description.authors.first()

    final override fun onPlaceholderRequest(
        player: Player?,
        params: String
    ): String? = super.onPlaceholderRequest(player, params)

    final override fun onRequest(
        player: OfflinePlayer?,
        params: String
    ): String? {
        val placeholder = placeholders.getIgnoreCase(params) ?: return null
        return when (player) {
            null -> this.asString(placeholder.first)
            is Player -> this.asString(placeholder.second?.invoke(player))
            else -> this.asString(placeholder.third?.invoke(player))
        }
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
