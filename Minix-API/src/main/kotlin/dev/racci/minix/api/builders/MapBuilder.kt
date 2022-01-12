@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import dev.racci.minix.api.annotations.MinixDsl
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView

/**
 * Map Builder Util.
 */
@MinixDsl
class MapBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<MapBuilder, MapMeta>(itemStack) {

    /**
     * Gets or Sets the map colour. A custom map colour will alter the display of the map
     * in an inventory slot.
     *
     * @since 0.1.5
     */
    var colour: Color?
        get() = meta.color
        set(colour) { meta.color = colour }

    /**
     * Gets or Sets the location name. A custom map colour will alter the display of the
     * map in an inventory slot.
     *
     * @since 0.1.5
     */
    var locName: String?
        get() = meta.locationName
        set(name) { meta.locationName = name }

    /**
     * Gets or Sets if this map is scaling or not.
     *
     * @since 0.1.5
     */
    var scaling: Boolean
        get() = meta.isScaling
        set(scaling) { meta.isScaling = scaling }

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
        get() = meta.mapView
        set(view) { meta.mapView = view }
}
