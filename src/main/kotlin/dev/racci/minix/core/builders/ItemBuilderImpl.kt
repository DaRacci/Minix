@file:Suppress("UNUSED")

package dev.racci.minix.core.builders

import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.builders.BannerBuilder
import dev.racci.minix.api.builders.BookBuilder
import dev.racci.minix.api.builders.FireworkBuilder
import dev.racci.minix.api.builders.HeadBuilder
import dev.racci.minix.api.builders.ItemBuilder
import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.builders.MapBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.jetbrains.annotations.ApiStatus

/**
 * Item Builder.
 */
@MinixDsl
@ApiStatus.AvailableSince("0.1.5")
class ItemBuilderImpl internal constructor(
    itemStack: ItemStack,
) : BaseItemBuilderImpl<ItemBuilder, ItemMeta>(itemStack), ItemBuilder {

    companion object : ItemBuilderDSL {

        override fun from(
            itemStack: ItemStack,
            builder: ItemBuilder.() -> Unit,
        ): ItemStack = ItemBuilderImpl(itemStack).also(builder).build()

        override fun from(
            material: Material,
            builder: ItemBuilder.() -> Unit,
        ): ItemStack = ItemBuilderImpl(ItemStack(material)).also(builder).build()

        override fun banner(
            itemStack: ItemStack,
            builder: BannerBuilder.() -> Unit,
        ): ItemStack = BannerBuilderImpl(itemStack).also(builder).build()

        override fun book(
            itemStack: ItemStack,
            builder: BookBuilder.() -> Unit,
        ): ItemStack = BookBuilderImpl(itemStack).also(builder).build()

        override fun firework(
            itemStack: ItemStack,
            builder: FireworkBuilder.() -> Unit,
        ): ItemStack = FireworkBuilderImpl(itemStack).also(builder).build()

        override fun map(
            itemStack: ItemStack,
            builder: MapBuilder.() -> Unit,
        ): ItemStack = MapBuilderImpl(itemStack).also(builder).build()

        override fun head(
            itemStack: ItemStack,
            builder: HeadBuilder.() -> Unit,
        ): ItemStack = HeadBuilderImpl(itemStack).also(builder).build()
    }
}
