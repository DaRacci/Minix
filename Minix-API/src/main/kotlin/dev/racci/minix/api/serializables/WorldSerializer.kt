package dev.racci.minix.api.serializables

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.World
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object WorldSerializer : KSerializer<World> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("World", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: World
    ) = encoder.encodeString(value.name)

    override fun deserialize(decoder: Decoder): World {
        val name = decoder.decodeString()
        return Bukkit.getWorld(name) ?: error("No world $name found")
    }

    object Configurate : TypeSerializer<World> {

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
}
