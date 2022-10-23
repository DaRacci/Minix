package dev.racci.minix.data.serializers.kotlin.minecraft

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType

public object PatternSerializer : KSerializer<Pattern> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Pattern", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Pattern
    ): Unit = encoder.encodeString("${value.color}:${value.pattern}")

    override fun deserialize(decoder: Decoder): Pattern {
        val (colour, type) = decoder.decodeString().split(':', limit = 2)
        return Pattern(DyeColor.valueOf(colour), PatternType.valueOf(type))
    }
}
