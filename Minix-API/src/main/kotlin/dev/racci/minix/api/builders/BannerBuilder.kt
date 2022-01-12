@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import com.destroystokyo.paper.MaterialSetTag
import dev.racci.minix.api.annotations.MinixDsl
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

/**
 * Banner Builder Util.
 */
@MinixDsl
class BannerBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<BannerBuilder, BannerMeta>(itemStack) {

    /**
     * Get or set the patterns of the banner.
     */
    var patterns: List<Pattern>
        get() = meta.patterns
        set(patterns) { meta.patterns = patterns }

    /**
     * Set the base colour of the banner via a new [Material].
     * Will only set the type if the value is in [MaterialSetTag.BANNERS].
     */
    var baseColour: Material
        get() = itemStack.type
        set(material) {
            if(material in MaterialSetTag.BANNERS.values) {
                itemStack.type = material
            }
        }

    /**
     * Add the patterns onto the banner.
     */
    fun addPattern(vararg pattern: Pattern): BannerBuilder {
        pattern.forEach(meta::addPattern)
        return this
    }
}
