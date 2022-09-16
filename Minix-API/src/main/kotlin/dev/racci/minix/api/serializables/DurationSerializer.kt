@file:Suppress("Unused")

package dev.racci.minix.api.serializables

import dev.racci.minix.api.extensions.inWholeTicks
import dev.racci.minix.api.extensions.ticks
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object DurationSerializer : KSerializer<Duration> {
    private val regex: Regex = Regex("(?<value>[0-9]+.[0-9]+)(?<unit>[a-zA-Z]+)") // Dot between numbers, so we can't match 1.1.1.1s.

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Time", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Duration
    ) = encoder.encodeString(value.inWholeMilliseconds.toString())

    override fun deserialize(decoder: Decoder): Duration {
        val string = decoder.decodeString()
        return fromString(string) ?: error("Invalid Duration: $string")
    }

    private fun toString(duration: Duration): String {
        (duration.inWholeDays % 7).takeIf { it > 0 }?.let { return "${it}w" }
        (duration.inWholeDays).takeIf { it > 0 }?.let { return "${it}d" }
        (duration.inWholeHours).takeIf { it > 0 }?.let { return "${it}h" }
        (duration.inWholeMinutes).takeIf { it > 0 }?.let { return "${it}m" }
        (duration.inWholeSeconds).takeIf { it > 0 }?.let { return "${it}s" }
        (duration.inWholeTicks).takeIf { it > 0 }?.let { return "${it}t" }
        return "${duration.inWholeMilliseconds}ms"
    }

    private fun fromString(string: String): Duration? {
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

    object Configurate : TypeSerializer<Duration> {
        override fun serialize(
            type: Type,
            obj: Duration?,
            node: ConfigurationNode
        ) {
            if (obj == null) { node.raw(null); return }
            node.set(toString(obj))
        }

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): Duration = node.get<String>()?.let(::fromString) ?: error("Invalid Duration: ${node.get<String>()}")
    }
}
