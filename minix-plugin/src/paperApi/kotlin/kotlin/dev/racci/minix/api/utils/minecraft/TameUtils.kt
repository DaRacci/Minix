package dev.racci.minix.api.utils.minecraft

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable

public object TameUtils {

    public fun isTamed(entity: Entity): Boolean = (entity as? Tameable)?.isTamed == true

    public fun hasOwner(entity: Entity): Boolean = (entity as? Tameable)?.owner != null

    public fun isOwner(
        player: Player,
        entity: Entity
    ): Boolean = (entity as? Tameable)?.owner?.uniqueId == player.uniqueId
}
