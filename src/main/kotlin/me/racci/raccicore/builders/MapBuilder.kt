package me.racci.raccicore.builders

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView

class MapBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<MapBuilder>(itemStack) {

    private val mMeta = meta.clone() as MapMeta

    /**
     * Sets the map colour. A custom map colour will alter the display of the map
     * in an inventory slot.
     *
     * @param colour the colour to set
     * @return [MapBuilder]
     * @since 0.1.5
     */
    fun colour(colour: Color?): MapBuilder {
        mMeta.color = colour
        return this
    }
    var colour: Color?
        get() = mMeta.color
        set(colour) {mMeta.color = colour}

    /**
     * Sets the location name. A custom map colour will alter the display of the
     * map in an inventory slot.
     *
     * @param name the name to set
     * @return [MapMeta]
     * @since 0.1.5
     */
    fun locationName(name: String): MapBuilder {
        mMeta.locationName = name
        return this
    }
    var locName: String?
        get() = mMeta.locationName
        set(name) {mMeta.locationName = name}

    /**
     * Sets if this map is scaling or not.
     *
     * @param scaling true to scale
     * @return [MapMeta]
     * @since 0.1.5
     */
    fun scaling(scaling: Boolean): MapBuilder {
        mMeta.isScaling = scaling
        return this
    }
    var scaling: Boolean
        get() = mMeta.isScaling
        set(scaling) {mMeta.isScaling = scaling}

    /**
     * Sets the associated map. This is used to determine what map is displayed.
     *
     *
     * The implementation **may** allow null to clear the associated map, but
     * this is not required and is liable to generate a new (undefined) map when
     * the item is first used.
     *
     * @param view the map to set
     * @return [MapBuilder]
     * @since 0.1.5
     */
    fun view(view: MapView): MapBuilder {
        mMeta.mapView = view
        return this
    }
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