package dev.racci.minix.data.serializers.configurate

import org.bukkit.Bukkit
import org.bukkit.World
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateWorldSerializer : TypeSerializer<World> {

    override fun serialize(
        type: Type,
        obj: World?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null); return
        }
        node.set(obj.name)
    }

    override fun deserialize(
        type: Type?,
        node: ConfigurationNode?
    ): World = node?.get<String>()?.let(Bukkit::getWorld) ?: error("No world found")
}
