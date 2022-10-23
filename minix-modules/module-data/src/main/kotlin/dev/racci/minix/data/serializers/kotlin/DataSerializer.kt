package dev.racci.minix.data.serializers.kotlin

import dev.racci.minix.data.DataSize
import dev.racci.minix.data.serializers.base.DataSerializerBase
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object DataSerializer : DataSerializerBase, KSerializer<DataSize> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun deserialize(decoder: Decoder): DataSize {
        val match = decoder.decodeString().let(DataSize.regex::matchEntire)
        return super.deserialize(match)
    }

    override fun serialize(
        encoder: Encoder,
        value: DataSize
    ) {
        encoder.encodeString(super.serialize(value))
    }
}
