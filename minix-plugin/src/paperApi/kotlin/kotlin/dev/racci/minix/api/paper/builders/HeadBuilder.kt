package dev.racci.minix.api.paper.builders

import com.destroystokyo.paper.profile.PlayerProfile
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.meta.SkullMeta

public interface HeadBuilder : BaseItemBuilder<HeadBuilder, SkullMeta> {

    /**
     * Set the texture of the head.
     * Has a defined UUID to be consistent.
     */
    public var texture: String

    /**
     * Get or set the owner of the head.
     */
    public var owner: OfflinePlayer?

    /**
     * Get or set the [PlayerProfile] of the head.
     */
    public var playerProfiler: PlayerProfile?
}
