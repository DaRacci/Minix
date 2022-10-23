package dev.racci.minix.data.serializers.kotlin

import dev.racci.minix.data.ranges.DoubleRange
import dev.racci.minix.data.ranges.FloatRange
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public abstract class RangeSerializer<T : ClosedRange<*>> : KSerializer<T> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("range", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: T
    ): Unit = encoder.encodeString(value.toStringWithSingleDigit())

    public abstract class Configurate<T : ClosedRange<*>> : TypeSerializer<T> {

        override fun serialize(
            type: Type,
            obj: T?,
            node: ConfigurationNode
        ) {
            if (obj == null) {
                node.raw(null); return
            }
            node.set(obj.toStringWithSingleDigit())
        }
    }
}

/**
 * A serializer for [IntRange]s which parses input as `min..max`, `min to max`, or one value for both min and max.
 */
public object IntRangeSerializer : RangeSerializer<IntRange>() {

    override fun deserialize(decoder: Decoder): IntRange {
        val (min, max) = valuesForRange(decoder.decodeString()) { toInt() }
        return min..max
    }

    public object Configurate : RangeSerializer.Configurate<IntRange>() {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): IntRange {
            val (min, max) = node.get<String>()?.let { valuesForRange(it) { toInt() } } ?: throw SerializationException(
                type,
                "Could not parse range: ${node.get<String>()}"
            )
            return min..max
        }
    }
}

/**
 * A serializer for [LongRange]s which parses input as `min..max`, `min to max`, or one value for both min and max.
 */
public object LongRangeSerializer : RangeSerializer<LongRange>() {

    override fun deserialize(decoder: Decoder): LongRange {
        val (min, max) = valuesForRange(decoder.decodeString()) { toLong() }
        return min..max
    }

    public object Configurate : RangeSerializer.Configurate<LongRange>() {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): LongRange {
            val (min, max) = node.get<String>()?.let { valuesForRange(it) { toLong() } } ?: throw SerializationException(
                type,
                "Could not parse range: ${node.get<String>()}"
            )
            return min..max
        }
    }
}

/**
 * A serializer for [CharRange]s which parses input as `min..max`, `min to max`, or one value for both min and max.
 */
public object CharRangeSerializer : RangeSerializer<CharRange>() {

    override fun deserialize(decoder: Decoder): CharRange {
        val (min, max) = valuesForRange(decoder.decodeString()) { get(0) }
        return min..max
    }

    public object Configurate : RangeSerializer.Configurate<CharRange>() {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): CharRange {
            val (min, max) = node.get<String>()?.let { valuesForRange(it) { get(0) } } ?: throw SerializationException(
                type,
                "Could not parse range: ${node.get<String>()}"
            )
            return min..max
        }
    }
}

/**
 * A serializer for [DoubleRange]s which parses input as `min..max`, `min to max`, or one value for both min and max.
 */
public object DoubleRangeSerializer : RangeSerializer<DoubleRange>() {

    override fun deserialize(decoder: Decoder): DoubleRange {
        val (min, max) = valuesForRange(decoder.decodeString()) { toDouble() }
        return min..max
    }

    public object Configurate : RangeSerializer.Configurate<DoubleRange>() {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): DoubleRange {
            val (min, max) = node.get<String>()?.let { valuesForRange(it) { toDouble() } } ?: throw SerializationException(
                type,
                "Could not parse range: ${node.get<String>()}"
            )
            return min..max
        }
    }
}

/**
 * A serializer for [FloatRange]s which parses input as `min..max`, `min to max`, or one value for both min and max.
 */
public object FloatRangeSerializer : RangeSerializer<FloatRange>() {

    override fun deserialize(decoder: Decoder): FloatRange {
        val (min, max) = valuesForRange(decoder.decodeString()) { toFloat() }
        return min..max
    }

    public object Configurate : RangeSerializer.Configurate<FloatRange>() {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): FloatRange {
            val (min, max) = node.get<String>()?.let { valuesForRange(it) { toFloat() } } ?: throw SerializationException(
                type,
                "Could not parse range: ${node.get<String>()}"
            )
            return min..max
        }
    }
}

private inline fun <T> valuesForRange(
    string: String,
    map: String.() -> T
): Pair<T, T> {
    val range = string.split("..", " to ").map(map)

    assert(range.size <= 2) {
        "Malformed range: $string, must follow format min..max, min to max, or one value for min/max"
    }

    // if only one element first and last are that one element.
    return range.first() to range.last()
}

private fun ClosedRange<*>.toStringWithSingleDigit() = if (start == endInclusive) {
    start.toString()
} else "$start..$endInclusive"
