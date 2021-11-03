package me.racci.raccicore.builders

import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta

// TODO add shield()
@Suppress("DEPRECATION")
class BannerBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BannerBuilder>(itemStack) {

    private var bItemStack = itemStack.clone()
        get() {field.itemMeta = bMeta;return field}

    private val bMeta = meta.clone() as BannerMeta

    var baseColour: DyeColor?
        get() = bMeta.baseColor
        set(colour) {bMeta.baseColor = colour}
    fun baseColour(colour: DyeColor): BannerBuilder {
        bMeta.baseColor = colour
        return this
    }

    fun pattern(vararg pairs: Pair<DyeColor, PatternType>): BannerBuilder {
        bMeta.patterns.addAll(pairs.map{Pattern(it.first, it.second)})
        return this
    }

    fun pattern(vararg pattern: Pattern): BannerBuilder {
        pattern.forEach(bMeta::addPattern)
        return this
    }

    fun pattern(index: Int, color: DyeColor, pattern: PatternType): BannerBuilder {
        return pattern(index, Pattern(color, pattern))
    }

    private fun pattern(index: Int, pattern: Pattern): BannerBuilder {
        bMeta.setPattern(index, pattern)
        return this
    }

    fun setPatterns(patterns: List<Pattern>): BannerBuilder {
        bMeta.patterns = patterns
        return this
    }
    var patterns : List<Pattern>
        get() = bMeta.patterns
        set(patterns) {bMeta.patterns = patterns}

    override fun build(): ItemStack {
        bItemStack.itemMeta = bMeta
        return bItemStack
    }
}