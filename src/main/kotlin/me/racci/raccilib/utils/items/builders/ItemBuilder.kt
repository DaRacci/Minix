package me.racci.raccilib.utils.items.builders

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Main ItemBuilder
 */
class ItemBuilder
/**
 * Constructor of the item builder
 *
 * @param itemStack The [ItemStack] of the item
 */
internal constructor(itemStack: ItemStack) : BaseItemBuilder<ItemBuilder>(itemStack) {

    companion object {
        /**
         * Main method to create [ItemBuilder]
         *
         * @param itemStack The [ItemStack] you want to edit
         * @return A new [ItemBuilder]
         */
        fun from(itemStack: ItemStack): ItemBuilder {
            return ItemBuilder(itemStack)
        }

        /**
         * Alternative method to create [ItemBuilder]
         *
         * @param material The [Material] you want to create an item from
         * @return A new [ItemBuilder]
         */
        fun from(material: Material): ItemBuilder {
            return ItemBuilder(ItemStack(material))
        }

        /**
         * Method for creating a [BannerBuilder] which will have BANNER specific methods
         *
         * @return A new [BannerBuilder]
         * @since 3.0.1
         */
        fun banner(): BannerBuilder {
            return BannerBuilder()
        }

        /**
         * Method for creating a [BannerBuilder] which will have BANNER specific methods
         *
         * @param itemStack An existing BANNER [ItemStack]
         * @return A new [BannerBuilder]
         * @throws UnsupportedOperationException if the item is not a BANNER
         * @since 3.0.1
         */
        fun banner(itemStack: ItemStack): BannerBuilder {
            return BannerBuilder(itemStack)
        }

        /**
         * Method for creating a [BookBuilder] which will have [Material.WRITABLE_BOOK] /
         * [Material.WRITTEN_BOOK] specific methods
         *
         * @param itemStack an existing [Material.WRITABLE_BOOK] / [Material.WRITTEN_BOOK] [ItemStack]
         * @return A new [FireworkBuilder]
         * @throws UnsupportedOperationException if the item type is not [Material.WRITABLE_BOOK]
         * or [Material.WRITTEN_BOOK]
         * @since 3.0.1
         */
        fun book(itemStack: ItemStack): BookBuilder {
            return BookBuilder(itemStack)
        }

        /**
         * Method for creating a [FireworkBuilder] which will have [Material.FIREWORK_ROCKET] specific methods
         *
         * @return A new [FireworkBuilder]
         * @since 3.0.1
         */
        fun firework(): FireworkBuilder {
            return FireworkBuilder(ItemStack(Material.FIREWORK_ROCKET))
        }

        /**
         * Method for creating a [FireworkBuilder] which will have [Material.FIREWORK_ROCKET] specific methods
         *
         * @param itemStack an existing [Material.FIREWORK_ROCKET] [ItemStack]
         * @return A new [FireworkBuilder]
         * @throws UnsupportedOperationException if the item type is not [Material.FIREWORK_ROCKET]
         * @since 3.0.1
         */
        fun firework(itemStack: ItemStack): FireworkBuilder {
            return FireworkBuilder(itemStack)
        }

        /**
         * Method for creating a [MapBuilder] which will have [Material.MAP] specific methods
         *
         * @return A new [MapBuilder]
         * @since 3.0.1
         */
        fun map(): MapBuilder {
            return MapBuilder()
        }

        /**
         * Method for creating a [MapBuilder] which will have [Material.MAP] specific methods
         *
         * @param itemStack An existing [Material.MAP] [ItemStack]
         * @return A new [MapBuilder]
         * @throws UnsupportedOperationException if the item type is not [Material.MAP]
         * @since 3.0.1
         */
        fun map(itemStack: ItemStack): MapBuilder {
            return MapBuilder(itemStack)
        }

        /**
         * Method for creating a [SkullBuilder] which will have PLAYER_HEAD specific methods
         *
         * @return A new [SkullBuilder]
         */
        fun skull(): SkullBuilder {
            return SkullBuilder()
        }

        /**
         * Method for creating a [SkullBuilder] which will have PLAYER_HEAD specific methods
         *
         * @param itemStack An existing PLAYER_HEAD [ItemStack]
         * @return A new [SkullBuilder]
         * @throws UnsupportedOperationException if the item is not a player head
         */
        fun skull(itemStack: ItemStack): SkullBuilder {
            return SkullBuilder(itemStack)
        }

        /**
         * Method for creating a [FireworkBuilder] which will have [Material.FIREWORK_STAR] specific methods
         *
         * @return A new [FireworkBuilder]
         * @since 3.0.1
         */
        fun star(): FireworkBuilder {
            return FireworkBuilder(ItemStack(Material.FIREWORK_STAR))
        }

        /**
         * Method for creating a [FireworkBuilder] which will have [Material.FIREWORK_STAR] specific methods
         *
         * @param itemStack an existing [Material.FIREWORK_STAR] [ItemStack]
         * @return A new [FireworkBuilder]
         * @throws UnsupportedOperationException if the item type is not [Material.FIREWORK_STAR]
         * @since 3.0.1
         */
        fun star(itemStack: ItemStack): FireworkBuilder {
            return FireworkBuilder(itemStack)
        }
    }
}