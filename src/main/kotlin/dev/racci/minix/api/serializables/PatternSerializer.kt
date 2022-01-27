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
}
