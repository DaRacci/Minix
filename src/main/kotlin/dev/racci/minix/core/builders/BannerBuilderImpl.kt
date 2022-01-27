@file:Suppress("UNUSED")

package dev.racci.minix.core.builders

import com.destroystokyo.paper.MaterialSetTag
import dev.racci.minix.api.builders.BannerBuilder
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.jetbrains.annotations.ApiStatus

/**
 * Banner Builder Util.
 */
@ApiStatus.AvailableSince("0.1.5")
class BannerBuilderImpl internal constructor(
    itemStack: ItemStack,
) : BaseItemBuilderImpl<BannerBuilder, BannerMeta>(itemStack), BannerBuilder {

    override var patterns: List<Pattern>
        get() = meta.patterns
        set(patterns) {
            meta.patterns = patterns
        }

    override var baseColour: Material
        get() = itemStack.type
        set(material) {
            if (material in MaterialSetTag.BANNERS.values) {
                itemStack.type = material
            }
        }

    override fun addPattern(vararg pattern: Pattern): BannerBuilder {
        pattern.forEach(meta::addPattern)
        return this
    }
}
