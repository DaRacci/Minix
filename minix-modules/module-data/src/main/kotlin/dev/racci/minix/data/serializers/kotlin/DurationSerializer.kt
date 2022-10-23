package dev.racci.minix.data.serializers.kotlin

import dev.racci.minix.api.utils.ticks
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

public object DurationSerializer : KSerializer<Duration> {
    private val regex: Regex = Regex("(?<value>[0-9]+.[0-9]+)(?<unit>[a-zA-Z]+)") // Dot between numbers, so we can't match 1.1.1.1s.

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Time", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Duration
    ): Unit = encoder.encodeString(value.inWholeMilliseconds.toString())

    override fun deserialize(decoder: Decoder): Duration {
        val string = decoder.decodeString()
        return fromString(string) ?: error("Invalid Duration: $string")
    }

    internal fun toString(duration: Duration): String {
        (duration.inWholeDays % 7).takeIf { it > 0 }?.let { return "${it}w" }
        (duration.inWholeDays).takeIf { it > 0 }?.let { return "${it}d" }
        (duration.inWholeHours).takeIf { it > 0 }?.let { return "${it}h" }
        (duration.inWholeMinutes).takeIf { it > 0 }?.let { return "${it}m" }
        (duration.inWholeSeconds).takeIf { it > 0 }?.let { return "${it}s" }
        (duration.inWholeMilliseconds / 50).takeIf { it > 0 }?.let { return "${it}t" }
        return "${duration.inWholeMilliseconds}ms"
    }

    internal fun fromString(string: String): Duration? {
        val match = regex.matchEntire(string) ?: return null
        val value = match.groupValues[0].toDouble()
        return when (match.groupValues[1]) {
            "w" -> value.days * 7
            "d" -> value.days
            "h" -> value.hours
            "m" -> value.minutes
            "s" -> value.seconds
            "t" -> value.ticks
            "ms" -> value.milliseconds
            else -> null
        }
    }
}
