package me.racci.raccicore.utils.items.builders

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import java.util.*

/**
 * Item builder for banners only
 *
 * @author GabyTM [https://github.com/iGabyTM](https://github.com/iGabyTM)
 * @since 3.0.1
 */
class BannerBuilder : BaseItemBuilder<BannerBuilder> {
    companion object {
        private val DEFAULT_BANNER: Material = Material.WHITE_BANNER
        private val BANNERS: EnumSet<Material> = EnumSet.copyOf(Tag.BANNERS.values)
    }

    internal constructor() : super(ItemStack(DEFAULT_BANNER))
    internal constructor(itemStack: ItemStack) : super(itemStack)

    /**
     * Sets the base color for this banner
     *
     * @param color the base color
     * @return [BannerBuilder]
     * @since 3.0.1
     */
    fun baseColor(color: DyeColor): BannerBuilder {
        val bannerData = meta as BannerMeta
        bannerData.baseColor = color
        meta = bannerData
        return this
    }

    /**
     * Adds a new pattern on top of the existing patterns
     *
     * @param color   the pattern color
     * @param pattern the pattern type
     * @return [BannerBuilder]
     * @since 3.0.1
     */
    fun pattern(color: DyeColor, pattern: PatternType): BannerBuilder {
        val bannerMeta = meta as BannerMeta
        bannerMeta.addPattern(Pattern(color, pattern))
        meta = bannerMeta
        return this
    }

    /**
     * Adds new patterns on top of the existing patterns
     *
     * @param pattern the patterns
     * @return [BannerBuilder]
     * @since 3.0.1
     */
    fun pattern(vararg pattern: Pattern?): BannerBuilder {
        return pattern(listOf(*pattern))
    }

    /**
     * Adds new patterns on top of the existing patterns
     *
     * @param patterns the patterns
     * @return [BannerBuilder]
     * @since 3.0.1
     */
    private fun pattern(patterns: List<Pattern?>): BannerBuilder {
        val bannerMeta = meta as BannerMeta
        for (it in patterns) {
            bannerMeta.addPattern(it!!)
        }
        meta = bannerMeta
        return this
    }

    /**
     * Sets the pattern at the specified index
     *
     * @param index   the index
     * @param color   the pattern color
     * @param pattern the pattern type
     * @return [BannerBuilder]
     * @throws IndexOutOfBoundsException when index is not in [0, [BannerMeta.numberOfPatterns]) range
     * @since 3.0.1
     */
    fun pattern(index: Int, color: DyeColor, pattern: PatternType): BannerBuilder {
        return pattern(index, Pattern(color, pattern))
    }

    /**
     * Sets the pattern at the specified index
     *
     * @param index   the index
     * @param pattern the new pattern
     * @return [BannerBuilder]
     * @throws IndexOutOfBoundsException when index is not in [0, [BannerMeta.numberOfPatterns]) range
     * @since 3.0.1
     */
    private fun pattern(index: Int, pattern: Pattern): BannerBuilder {
        val bannerMeta = meta as BannerMeta
        bannerMeta.setPattern(index, pattern)
        meta = bannerMeta
        return this
    }

    /**
     * Sets the patterns used on this banner
     *
     * @param patterns the new list of patterns
     * @return [BannerBuilder]
     * @since 3.0.1
     */
    fun setPatterns(patterns: List<Pattern?>): BannerBuilder {
        val bannerMeta = meta as BannerMeta
        bannerMeta.patterns = patterns
        meta = bannerMeta
        return this
    } // TODO add shield()
}