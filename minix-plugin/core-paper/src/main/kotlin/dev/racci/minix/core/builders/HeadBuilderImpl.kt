package dev.racci.minix.core.builders

import com.destroystokyo.paper.profile.PlayerProfile
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.paper.builders.HeadBuilder
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

public class HeadBuilderImpl internal constructor(
    itemStack: ItemStack
) : BaseItemBuilderImpl<HeadBuilder, SkullMeta>(itemStack), HeadBuilder {
    private val logger by MinixLoggerFactory

    // TODO -> Add back support
    @Suppress("CastToNullableType")
    override var texture: String
        get() = throw UnsupportedOperationException("Please don't make me implement this")
        set(texture) {
            logger.warn { "Adding textures to heads is currently non functional." }
//            meta.addTexture(texture)
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
