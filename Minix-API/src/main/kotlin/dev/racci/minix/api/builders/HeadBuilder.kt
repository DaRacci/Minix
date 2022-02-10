package dev.racci.minix.api.builders

import com.destroystokyo.paper.profile.PlayerProfile
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.meta.SkullMeta

interface HeadBuilder : BaseItemBuilder<HeadBuilder, SkullMeta> {

    /**
     * Set the texture of the head.
     * Has a defined UUID to be consistent.
     */
    @Suppress("CastToNullableType", "UNCHECKED_CAST")
    var texture: String

    /**
     * Get or set the owner of the head.
     */
    var owner: OfflinePlayer?

    /**
     * Get or set the [PlayerProfile] of the head.
     */
    var playerProfiler: PlayerProfile?
}
