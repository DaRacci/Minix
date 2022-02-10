package dev.racci.minix.api.utils.minecraft

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable

object TameUtils {

    fun isTamed(entity: Entity): Boolean = (entity as? Tameable)?.isTamed == true

    fun hasOwner(entity: Entity): Boolean = (entity as? Tameable)?.owner != null

    fun isOwner(
        player: Player,
        entity: Entity
    ): Boolean = (entity as? Tameable)?.owner?.uniqueId == player.uniqueId
}
