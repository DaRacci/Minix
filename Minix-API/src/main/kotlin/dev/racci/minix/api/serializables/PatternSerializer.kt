package dev.racci.minix.api.serializables

import dev.racci.minix.api.destructors.component1
import dev.racci.minix.api.destructors.component2
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object PatternSerializer : KSerializer<Pattern> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pattern", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Pattern,
    ) {
        val (colour, type) = value
        encoder.encodeString("$colour:$type")
    }

    override fun deserialize(decoder: Decoder): Pattern {
        val (colour, type) = decoder.decodeString().split(':', limit = 2)
        return Pattern(DyeColor.valueOf(colour), PatternType.valueOf(type))
    }

    object Configurate : TypeSerializer<Pattern> {

        override fun serialize(
            type: Type,
            obj: Pattern?,
            node: ConfigurationNode,
        ) {
            if (obj == null) {
                node.raw(null); return
            }
            node.set("${obj.color.name}:${obj.pattern}")
        }

        override fun deserialize(
            type: Type,
            node: ConfigurationNode,
        ): Pattern = node.get<String>()?.split(':', limit = 2)?.let { Pattern(DyeColor.valueOf(it[0]), PatternType.valueOf(it[1])) }
            ?: error("Invalid pattern, expected [colour:pattern]")
    }
}
