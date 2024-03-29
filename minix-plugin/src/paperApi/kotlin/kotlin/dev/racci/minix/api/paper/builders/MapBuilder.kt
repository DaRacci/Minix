package dev.racci.minix.api.paper.builders

import org.bukkit.Color
import org.bukkit.map.MapView

public interface MapBuilder {

    /**
     * Gets or Sets the map colour.
     * A custom map colour will alter the display of the map
     * in an inventory slot.
     */
    public var colour: Color?

    /**
     * Gets or Sets the location name.
     * A custom map colour will alter the display of the
     * map in an inventory slot.
     */
    public var locName: String?

    /**
     * Gets or Sets if this map is scaling or not.
     */
    public var scaling: Boolean

    /**
     * Gets or Sets the associated map.
     * This is used to determine what map is displayed.
     *
     * The implementation **may** allow null to clear the associated map, but
     * this is not required and is liable to generate a new (undefined) map when
     * the item is first used.
     */
    public var view: MapView?
}
