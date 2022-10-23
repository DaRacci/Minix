package dev.racci.minix.api.paper.builders

import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.exceptions.IncorrectItemTypeException
import dev.racci.minix.api.utils.getKoin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

public interface ItemBuilderDSL {

    /**
     * Method for creating a new [ItemBuilderDSL].
     *
     * @param itemStack an existing [ItemStack]
     * @param builder the dsl block.
     * @return A new [ItemBuilderDSL]
     */
    @MinixDsl
    public fun from(
        itemStack: ItemStack,
        builder: ItemBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a new [ItemBuilderDSL].
     *
     * @param material the [Material] of the new [ItemStack]
     * @param builder the dsl block.
     * @return A new [ItemBuilderDSL]
     */
    @MinixDsl
    public fun from(
        material: Material,
        builder: ItemBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a new [BannerBuilder] which includes
     * banner specific methods.
     *
     * @param itemStack an existing banner [ItemStack]
     * if none is supplied a new [Material.WHITE_BANNER] [ItemStack] is created.
     * @param builder the dsl block.
     * @return A new [BannerBuilder]
     */
    @MinixDsl
    @Throws(IncorrectItemTypeException::class)
    public fun banner(
        itemStack: ItemStack = ItemStack(Material.WHITE_BANNER),
        builder: BannerBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a [BookBuilder] which includes
     * book specific methods.
     *
     * @param itemStack an existing book [ItemStack],
     * if none is supplied a new [Material.WRITTEN_BOOK] [ItemStack] is created.
     * @param builder the dsl block.
     * @return A new [BookBuilder]
     */
    @MinixDsl
    @Throws(IncorrectItemTypeException::class)
    public fun book(
        itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK),
        builder: BookBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a [FireworkBuilder] which includes
     * firework specific methods.
     *
     * @param itemStack an existing firework [ItemStack]
     * if none is supplied a new [Material.FIREWORK_ROCKET] [ItemStack] is created.
     * @param builder the dsl block.
     * @return A new [FireworkBuilder]
     */
    @MinixDsl
    @Throws(IncorrectItemTypeException::class)
    public fun firework(
        itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET),
        builder: FireworkBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a [MapBuilder] which includes
     * map specific methods.
     *
     * @param itemStack An existing map [ItemStack]
     * if none is supplied a new [Material.MAP] [ItemStack] is created.
     * @param builder the dsl block.
     * @return A new [MapBuilder]
     */
    @MinixDsl
    @Throws(IncorrectItemTypeException::class)
    public fun map(
        itemStack: ItemStack = ItemStack(Material.MAP),
        builder: MapBuilder.() -> Unit = {}
    ): ItemStack

    /**
     * Method for creating a [HeadBuilder] which includes
     * player head specific methods.
     *
     * @param itemStack An existing player head [ItemStack]
     * if none is supplied a new [Material.PLAYER_HEAD] [ItemStack] is created.
     * @param builder the dsl block.
     * @return A new [HeadBuilder]
     */
    @MinixDsl
    @Throws(IncorrectItemTypeException::class)
    public fun head(
        itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD),
        builder: HeadBuilder.() -> Unit = {}
    ): ItemStack

    public companion object : ItemBuilderDSL by getKoin().get()
}
