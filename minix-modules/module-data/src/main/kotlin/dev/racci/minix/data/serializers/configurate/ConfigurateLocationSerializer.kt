package dev.racci.minix.data.serializers.configurate

import org.bukkit.Location
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateLocationSerializer : TypeSerializer<Location> {

    override fun serialize(
        type: Type,
        obj: Location?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null); return
        }
        node.set(obj.serialize())
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Location = node.get<Map<String, Any>>()?.let(Location::deserialize) ?: error("Could not deserialize location")
}
