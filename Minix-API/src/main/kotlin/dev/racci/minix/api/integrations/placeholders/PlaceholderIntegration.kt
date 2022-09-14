package dev.racci.minix.api.integrations.placeholders

import dev.racci.minix.api.integrations.Integration
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

abstract class PlaceholderIntegration : PlaceholderExpansion(), Integration {
    private val placeholders = mutableMapOf<String, Triple<(() -> Any)?, (Player.() -> Any)?, (OfflinePlayer.() -> Any)?>>()

    /** Registers a placeholder, which doesn't require a player. */
    @JvmName("registerNoPlayerPlaceholder")
    fun registerPlaceholder(
        placeholder: String,
        value: () -> String
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(value, oldTriple?.second, oldTriple?.third) }

    /** Registers a placeholder, which doesn't require a player. */
    @JvmName("registerNoPlayerPlaceholderComponent")
    fun registerPlaceholder(
        placeholder: String,
        value: () -> Component
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(value, oldTriple?.second, oldTriple?.third) }

    /** Registers a placeholder, which requires an online player. */
    @JvmName("registerOnlinePlayerPlaceholder")
    fun registerPlaceholder(
        placeholder: String,
        value: Player.() -> String
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(oldTriple?.first, value, oldTriple?.third) }

    /** Registers a placeholder, which requires an online player. */
    @JvmName("registerOnlinePlayerPlaceholderComponent")
    fun registerPlaceholder(
        placeholder: String,
        value: Player.() -> Component
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(oldTriple?.first, value, oldTriple?.third) }

    /** Registers a placeholder, which a player. */
    @JvmName("registerOfflinePlayerPlaceholder")
    fun registerPlaceholder(
        placeholder: String,
        value: OfflinePlayer.() -> String
    ) = placeholders.compute(placeholder) { _, oldTriple -> Triple(oldTriple?.first, oldTriple?.second, value) }

    /** Registers a placeholder, which a player. */
    @JvmName("registerOfflinePlayerPlaceholderComponent")
    fun registerPlaceholder(
        placeholder: String,
        value: OfflinePlayer.() -> Component
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
        val placeholder = placeholders[params] ?: return null
        return when (player) {
            null -> this.asString(placeholder.first)
            is Player -> this.asString(placeholder.second?.invoke(player))
            else -> this.asString(placeholder.third?.invoke(player))
        }
    }

    private fun asString(value: Any?): String? = when (value) {
        is String -> value
        is Component -> LegacyComponentSerializer.legacySection().serialize(value)
        else -> null
    }
}
