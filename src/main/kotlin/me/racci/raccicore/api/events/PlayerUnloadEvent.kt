package me.racci.raccicore.api.events

import me.racci.raccicore.api.utils.uuid
import org.bukkit.entity.Player
import java.util.*

/**
 * Called once it is safe to unload a Player's data.
 *
 * **Fired Asynchronously**
 *
 * @param player The Player if able to be provided.
 * @param uuid The UUID, this will always be provided.
 *
 * @since 0.3.0
 */
class PlayerUnloadEvent(
    val player: Player? = null,
    val uuid: UUID = player?.uuid ?: throw IllegalArgumentException()
): KEvent(true)