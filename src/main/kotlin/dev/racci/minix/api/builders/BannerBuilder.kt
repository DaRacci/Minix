package dev.racci.minix.api.builders

import com.destroystokyo.paper.MaterialSetTag
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.meta.BannerMeta

interface BannerBuilder : BaseItemBuilder<BannerBuilder, BannerMeta> {

    /**
     * Get or set the patterns of the banner.
     */
    var patterns: List<Pattern>

    /**
     * Set the base colour of the banner via a new [Material].
     * Will only set the type if the value is in [MaterialSetTag.BANNERS].
     */
    var baseColour: Material

    /**
     * Add the patterns onto the banner.
     */
    fun addPattern(vararg pattern: Pattern): BannerBuilder
}
