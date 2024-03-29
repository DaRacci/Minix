package dev.racci.minix.core.builders

import dev.racci.minix.api.paper.builders.MapBuilder
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView

public class MapBuilderImpl internal constructor(
    itemStack: ItemStack
) : BaseItemBuilderImpl<MapBuilderImpl, MapMeta>(itemStack), MapBuilder {

    override var colour: Color?
        get() = meta.color
        set(colour) {
            meta.color = colour
        }

    override var locName: String?
        get() = meta.locationName
        set(name) {
            meta.locationName = name
        }

    override var scaling: Boolean
        get() = meta.isScaling
        set(scaling) {
            meta.isScaling = scaling
        }

    override var view: MapView?
        get() = meta.mapView
        set(view) {
            meta.mapView = view
        }
}
