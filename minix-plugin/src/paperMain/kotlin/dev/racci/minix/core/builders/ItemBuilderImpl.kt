package dev.racci.minix.core.builders

import dev.racci.minix.api.paper.builders.BannerBuilder
import dev.racci.minix.api.paper.builders.BookBuilder
import dev.racci.minix.api.paper.builders.FireworkBuilder
import dev.racci.minix.api.paper.builders.HeadBuilder
import dev.racci.minix.api.paper.builders.ItemBuilder
import dev.racci.minix.api.paper.builders.ItemBuilderDSL
import dev.racci.minix.api.paper.builders.MapBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

public class ItemBuilderImpl internal constructor(
    itemStack: ItemStack
) : BaseItemBuilderImpl<ItemBuilder, ItemMeta>(itemStack), ItemBuilder {

    public companion object : ItemBuilderDSL {

        override fun from(
            itemStack: ItemStack,
            builder: ItemBuilder.() -> Unit
        ): ItemStack = ItemBuilderImpl(itemStack).also(builder).build()

        override fun from(
            material: Material,
            builder: ItemBuilder.() -> Unit
        ): ItemStack = ItemBuilderImpl(ItemStack(material)).also(builder).build()

        override fun banner(
            itemStack: ItemStack,
            builder: BannerBuilder.() -> Unit
        ): ItemStack = BannerBuilderImpl(itemStack).also(builder).build()

        override fun book(
            itemStack: ItemStack,
            builder: BookBuilder.() -> Unit
        ): ItemStack = BookBuilderImpl(itemStack).also(builder).build()

        override fun firework(
            itemStack: ItemStack,
            builder: FireworkBuilder.() -> Unit
        ): ItemStack = FireworkBuilderImpl(itemStack).also(builder).build()

        override fun map(
            itemStack: ItemStack,
            builder: MapBuilder.() -> Unit
        ): ItemStack = MapBuilderImpl(itemStack).also(builder).build()

        override fun head(
            itemStack: ItemStack,
            builder: HeadBuilder.() -> Unit
        ): ItemStack = HeadBuilderImpl(itemStack).also(builder).build()
    }
}
