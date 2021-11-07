package me.racci.raccicore.builders

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView

class MapBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<MapBuilder>(itemStack) {

    private val mMeta = meta.clone() as MapMeta

    /**
     * Gets or Sets the map colour. A custom map colour will alter the display of the map
     * in an inventory slot.
     *
     * @since 0.1.5
     */
    var colour: Color?
        get() = mMeta.color
        set(colour) {mMeta.color = colour}

    /**
     * Gets or Sets the location name. A custom map colour will alter the display of the
     * map in an inventory slot.
     *
     * @since 0.1.5
     */
    var locName: String?
        get() = mMeta.locationName
        set(name) {mMeta.locationName = name}

    /**
     * Gets or Sets if this map is scaling or not.
     *
     * @since 0.1.5
     */
    var scaling: Boolean
        get() = mMeta.isScaling
        set(scaling) {mMeta.isScaling = scaling}

    /**
     * Gets or Sets the associated map. This is used to determine what map is displayed.
     *
     * The implementation **may** allow null to clear the associated map, but
     * this is not required and is liable to generate a new (undefined) map when
     * the item is first used.
     *
     * @since 0.1.5
     */
    var view: MapView?
        get() = mMeta.mapView
        set(view) {mMeta.mapView = view}

    override fun build(): ItemStack {
        itemStack.itemMeta = mMeta
        return itemStack
    }

    init {
        if (itemStack.type != Material.MAP) {
            throw UnsupportedOperationException("BookBuilder requires the material to be a MAP!")
        }
    }
}