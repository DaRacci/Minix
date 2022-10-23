package dev.racci.minix.data.serializers.base

import dev.racci.minix.data.DataSize
import dev.racci.minix.data.exceptions.SerializationException
import kotlin.math.roundToLong

public interface DataSerializerBase {
    public fun deserialize(match: MatchResult?): DataSize {
        return when (match?.groups?.get("identifier")?.value?.lowercase()) {
            "b" -> DataSize(match.groups["size"]!!.value.toLong())
            "kb" -> DataSize.fromKilobytes(match.groups["size"]!!.value.toLong())
            "mb" -> DataSize.fromMegabytes(match.groups["size"]!!.value.toLong())
            "gb" -> DataSize.fromGigabytes(match.groups["size"]!!.value.toLong())
            else -> throw SerializationException(
                """
                        Unable to parse data from node:
                            Expected format: <value><identifier>
                            Expected identifiers: b, kb, mb, gb, tb
                            Received: ${match?.value}
                """.trimIndent()
            )
        }
    }

    public fun serialize(value: DataSize): String {
        return when {
            value.gigabytes > 0 -> "${dropTrailingZeros(value.gigabytes)}GB"
            value.megabytes > 0 -> "${dropTrailingZeros(value.megabytes)}MB"
            value.kilobytes > 0 -> "${dropTrailingZeros(value.kilobytes)}KB"
            else -> "${value.bytes}B"
        }
    }

    private fun dropTrailingZeros(value: Double): String {
        return if (value.roundToLong().toDouble() == value) {
            value.roundToLong().toString()
        } else value.toString()
    }
}
