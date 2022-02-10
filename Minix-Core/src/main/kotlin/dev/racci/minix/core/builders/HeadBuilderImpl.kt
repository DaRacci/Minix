@file:Suppress("UNUSED")

package dev.racci.minix.core.builders

import com.destroystokyo.paper.profile.PlayerProfile
import dev.racci.minix.api.builders.HeadBuilder
import dev.racci.minix.api.extensions.addTexture
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class HeadBuilderImpl internal constructor(
    itemStack: ItemStack,
) : BaseItemBuilderImpl<HeadBuilder, SkullMeta>(itemStack), HeadBuilder {

    @Suppress("CastToNullableType", "UNCHECKED_CAST")
    override var texture: String
        get() = throw UnsupportedOperationException("Please don't make me implement this")
        set(texture) {
            meta.addTexture(texture)
        }

    override var owner: OfflinePlayer?
        get() = meta.owningPlayer
        set(player) {
            meta.owningPlayer = player
        }

    override var playerProfiler: PlayerProfile?
        get() = meta.playerProfile
        set(playerProfile) {
            meta.playerProfile = playerProfile
        }
}
