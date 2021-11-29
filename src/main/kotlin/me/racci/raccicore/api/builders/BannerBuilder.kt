@file:Suppress("UNUSED")
package me.racci.raccicore.api.builders

import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

// TODO add shield()
@Suppress("DEPRECATION")
class BannerBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BannerBuilder>(itemStack) {

    var baseColour: DyeColor?
        get() = (meta as BannerMeta).baseColor
        set(colour) {(meta as BannerMeta).baseColor = colour}

    fun pattern(vararg pairs: Pair<DyeColor, PatternType>): BannerBuilder {
        (meta as BannerMeta).patterns.addAll(pairs.map{Pattern(it.first, it.second)})
        return this
    }

    fun pattern(vararg pattern: Pattern): BannerBuilder {
        pattern.forEach{(meta as BannerMeta).addPattern(it)}
        return this
    }

    fun pattern(index: Int, color: DyeColor, pattern: PatternType): BannerBuilder {
        return pattern(index, Pattern(color, pattern))
    }

    private fun pattern(index: Int, pattern: Pattern): BannerBuilder {
        (meta as BannerMeta).setPattern(index, pattern)
        return this
    }

    fun setPatterns(patterns: List<Pattern>): BannerBuilder {
        (meta as BannerMeta).patterns = patterns
        return this
    }
    var patterns : List<Pattern>
        get() = (meta as BannerMeta).patterns
        set(patterns) {(meta as BannerMeta).patterns = patterns}
}