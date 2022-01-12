@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import com.destroystokyo.paper.profile.PlayerProfile
import com.google.common.collect.Multimap
import dev.racci.minix.api.annotations.MinixDsl
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Head Builder Util.
 */
@MinixDsl
class HeadBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<HeadBuilder, SkullMeta>(itemStack) {

    /**
     * Set the texture of the head.
     * Has a defined UUID to be consistent.
     */
    var texture: String
        get() = throw UnsupportedOperationException("Please don't make me implement this")
        set(texture) {
            val profile = gameProfileConstructor.newInstance(UUID.fromString("38dff22c-c0ec-40b8-bd11-b4376e9a20a6"), null as String?)
            val properties = getPropertiesMethod.invoke(profile) as Multimap<Any, Any>
            properties.put("textures", propertyConstructor.newInstance("textures", texture))
            val profileField = meta.javaClass.getDeclaredField("profile").apply {isAccessible = true}
            profileField.set(meta, profile)
        }

    /**
     * Get or set the owner of the head.
     */
    var owner: OfflinePlayer?
        get() = meta.owningPlayer
        set(player) {meta.owningPlayer = player}

    /**
     * Get or set the [PlayerProfile] of the head.
     */
    var playerProfiler: PlayerProfile?
        get() = meta.playerProfile
        set(playerProfile) {meta.playerProfile = playerProfile}

}