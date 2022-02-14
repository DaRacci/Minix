@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.events

import org.bukkit.entity.Player
import java.util.UUID

/**
 * Called once it is safe to handleUnload a Player's data.
 *
 * ## This method is Fired Asynchronously
 *
 * @property player The Player if able to be provided.
 * @property uuid The UUID, this will always be provided.
 */
class PlayerUnloadEvent(
    val player: Player? = null,
    val uuid: UUID = player!!.uniqueId
) : KEvent(true) {

    operator fun component1(): Player? = player
    operator fun component2(): UUID = uuid
}
