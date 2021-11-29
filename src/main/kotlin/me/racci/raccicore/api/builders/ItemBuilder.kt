@file:Suppress("UNUSED")
package me.racci.raccicore.api.builders

import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class ItemBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<ItemBuilder>(itemStack) {

    companion object {

        /**
         * Method for creating a new [ItemBuilder]
         *
         * @param itemStack an existing [ItemStack]
         * @return A new [ItemBuilder]
         * @since 0.1.5
         */
        fun from(
            itemStack: ItemStack
        ): ItemBuilder = ItemBuilder(itemStack)
        fun from(
            itemStack: ItemStack,
            builder: ItemBuilder.() -> Unit = {}
        ) : ItemStack = ItemBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a new [ItemBuilder]
         *
         * @param material the [Material] of the new [ItemStack]
         * @return A new [ItemBuilder]
         * @since 0.1.5
         */
        fun from(
            material: Material
        ): ItemBuilder = ItemBuilder(ItemStack(material))
        fun from(
            material: Material,
            builder: ItemBuilder.() -> Unit = {}
        ) : ItemStack = ItemBuilder(ItemStack(material)).also(builder).build()

        /**
         * Method for creating a new [BannerBuilder] which includes
         * banner specific methods
         *
         * @param itemStack an existing banner [ItemStack]
         * if none is supplied a new [Material.WHITE_BANNER] [ItemStack] is created
         * @return A new [BannerBuilder]
         * @throws UnsupportedOperationException if the item is not a banner
         * @since 0.1.5
         */
        fun banner(
            itemStack: ItemStack = ItemStack(Material.WHITE_BANNER)
        ): BannerBuilder = BannerBuilder(itemStack)
        fun banner(
            itemStack: ItemStack = ItemStack(Material.WHITE_BANNER),
            builder: BannerBuilder.() -> Unit = {}
        ): ItemStack = BannerBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [BookBuilder] which includes
         * book specific methods
         *
         * @param itemStack an existing book [ItemStack],
         * if none is supplied a new [Material.WRITTEN_BOOK] [ItemStack] is created
         * @return A new [BookBuilder]
         * @throws UnsupportedOperationException if the item type is not a book
         * @since 0.1.5
         */
        fun book(
            itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK)
        ): BookBuilder = BookBuilder(itemStack)
        fun book(
            itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK),
            builder: BookBuilder.() -> Unit = {}
        ) : ItemStack = BookBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [FireworkBuilder] which includes
         * firework specific methods
         *
         * @param itemStack an existing firework [ItemStack]
         * if none is supplied a new [Material.FIREWORK_ROCKET] [ItemStack] is created
         * @return A new [FireworkBuilder]
         * @throws UnsupportedOperationException if the item type is not a firework
         * @since 0.1.5
         */
        fun firework(
            itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET)
        ): FireworkBuilder = FireworkBuilder(itemStack)
        fun firework(
            itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET),
            builder: FireworkBuilder.() -> Unit = {}
        ): ItemStack = FireworkBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [MapBuilder] which includes
         * map specific methods
         *
         * @param itemStack An existing map [ItemStack]
         * if none is supplied a new [Material.MAP] [ItemStack] is created
         * @return A new [MapBuilder]
         * @throws UnsupportedOperationException if the item type is not a map
         * @since 0.1.5
         */
        fun map(
            itemStack: ItemStack = ItemStack(Material.MAP)
        ): MapBuilder = MapBuilder(itemStack)
        fun map(
            itemStack: ItemStack = ItemStack(Material.MAP),
            builder: MapBuilder.() -> Unit = {}
        ): ItemStack = MapBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [HeadBuilder] which includes
         * player head specific methods
         *
         * @param itemStack An existing player head [ItemStack]
         * if none is supplied a new [Material.PLAYER_HEAD] [ItemStack] is created
         * @return A new [HeadBuilder]
         * @throws UnsupportedOperationException if the item is not a player head
         * @since 0.1.5
         */
        fun head(
            itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD)
        ): HeadBuilder = HeadBuilder(itemStack)
        fun head(
            itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD),
            builder: HeadBuilder.() -> Unit = {}
        ): ItemStack = HeadBuilder(itemStack).also(builder).build()

        /**
         * Method for creating a [FireworkBuilder] which includes
         * firework star specific methods
         *
         * @param itemStack an existing firework star [ItemStack]
         * if none is supplied a new [Material.FIREWORK_STAR] [ItemStack] is created
         * @return A new [FireworkBuilder]
         * @throws UnsupportedOperationException if the item type is not a firework star
         * @since 0.1.5
         */
        fun star(
            itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR)
        ): FireworkBuilder = FireworkBuilder(itemStack)
        fun star(
            itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR),
            builder: FireworkBuilder.() -> Unit = {}
        ): ItemStack = FireworkBuilder(itemStack).also(builder).build()
    }
}