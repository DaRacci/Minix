@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.utils.IncorrectItemTypeException
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Item Builder.
 */
@MinixDsl
class ItemBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<ItemBuilder, ItemMeta>(itemStack) {

    companion object {

        /**
         * Method for creating a new [ItemBuilder].
         *
         * @param itemStack an existing [ItemStack]
         * @param builder the dsl block.
         * @return A new [ItemBuilder]
         * @since 0.1.5
         */
        @MinixDsl
        fun from(
            itemStack: ItemStack,
            builder: ItemBuilder.() -> Unit = {}
        ) : ItemStack = ItemBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a new [ItemBuilder].
         *
         * @param material the [Material] of the new [ItemStack]
         * @param builder the dsl block.
         * @return A new [ItemBuilder]
         * @since 0.1.5
         */
        @MinixDsl
        fun from(
            material: Material,
            builder: ItemBuilder.() -> Unit = {}
        ) : ItemStack = ItemBuilder(ItemStack(material)).also(builder).build()

        /**
         * Method for creating a new [BannerBuilder] which includes
         * banner specific methods.
         *
         * @param itemStack an existing banner [ItemStack]
         * if none is supplied a new [Material.WHITE_BANNER] [ItemStack] is created
         * @param builder the dsl block.
         * @return A new [BannerBuilder]
         * @throws IncorrectItemTypeException if the item is not a banner
         * @since 0.1.5
         */
        @MinixDsl
        fun banner(
            itemStack: ItemStack = ItemStack(Material.WHITE_BANNER),
            builder: BannerBuilder.() -> Unit = {}
        ): ItemStack = BannerBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [BookBuilder] which includes
         * book specific methods.
         *
         * @param itemStack an existing book [ItemStack],
         * if none is supplied a new [Material.WRITTEN_BOOK] [ItemStack] is created
         * @param builder the dsl block.
         * @return A new [BookBuilder]
         * @throws IncorrectItemTypeException if the item type is not a book
         * @since 0.1.5
         */
        @MinixDsl
        fun book(
            itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK),
            builder: BookBuilder.() -> Unit = {}
        ) : ItemStack = BookBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [FireworkBuilder] which includes
         * firework specific methods.
         *
         * @param itemStack an existing firework [ItemStack]
         * if none is supplied a new [Material.FIREWORK_ROCKET] [ItemStack] is created
         * @param builder the dsl block.
         * @return A new [FireworkBuilder]
         * @throws IncorrectItemTypeException if the item type is not a firework
         * @since 0.1.5
         */
        @MinixDsl
        fun firework(
            itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET),
            builder: FireworkBuilder.() -> Unit = {}
        ): ItemStack = FireworkBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [MapBuilder] which includes
         * map specific methods.
         *
         * @param itemStack An existing map [ItemStack]
         * if none is supplied a new [Material.MAP] [ItemStack] is created
         * @param builder the dsl block.
         * @return A new [MapBuilder]
         * @throws IncorrectItemTypeException if the item type is not a map
         * @since 0.1.5
         */
        @MinixDsl
        fun map(
            itemStack: ItemStack = ItemStack(Material.MAP),
            builder: MapBuilder.() -> Unit = {}
        ): ItemStack = MapBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [HeadBuilder] which includes
         * player head specific methods.
         *
         * @param itemStack An existing player head [ItemStack]
         * if none is supplied a new [Material.PLAYER_HEAD] [ItemStack] is created
         * @param builder the dsl block.
         * @return A new [HeadBuilder]
         * @throws IncorrectItemTypeException if the item is not a player head
         * @since 0.1.5
         */
        @MinixDsl
        fun head(
            itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD),
            builder: HeadBuilder.() -> Unit = {}
        ): ItemStack = HeadBuilder(itemStack).also(builder).build()
    }
}
