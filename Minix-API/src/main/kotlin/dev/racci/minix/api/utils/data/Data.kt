package dev.racci.minix.api.utils.data

import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.getKoin
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import kotlin.math.roundToLong

data class Data(val bytes: Long) : Comparable<Data> {

    val kilobytes: Double by lazy { bytes / 1024.0 }
    val megabytes: Double by lazy { bytes / 1048576.0 }
    val gigabytes: Double by lazy { bytes / 1073741824.0 }

    val kilobytesRounded: Long by lazy { bytes / 1024 }
    val megabytesRounded: Long by lazy { bytes / 1048576 }
    val gigabytesRounded: Long by lazy { bytes / 1073741824 }

    override fun compareTo(other: Data): Int = when {
        bytes > other.bytes -> 1
        bytes < other.bytes -> -1
        else -> 0
    }

    // TODO: There has to be a better way to do this
    fun humanReadableSize(): String {
        return when {
            gigabytes > 0 -> "${gigabytes}GB"
            megabytes > 0 -> "${megabytes}MB"
            kilobytes > 0 -> "${kilobytes}KB"
            else -> "${bytes}B"
        }
    }

    override fun toString(): String {
        return "${bytes}B"
    }

    override fun hashCode(): Int {
        return bytes.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Data) {
            return bytes == other.bytes
        }
        return false
    }

    companion object {
        private val regex = Regex("(?<size>[0-9]+(\\.)?([0-9]+)?)(?<identifier>[a-zA-Z]+)")
        private val logger by lazy { getKoin().get<Minix>().log }

        fun fromKilobytes(kilobytes: Long): Data = Data(kilobytes * 1024)
        fun fromMegabytes(megabytes: Long): Data = Data(megabytes * 1048576)
        fun fromGigabytes(gigabytes: Long): Data = Data(gigabytes * 1073741824)
    }

    object Serializer : TypeSerializer<Data> {
        override fun deserialize(
            type: Type,
            node: ConfigurationNode,
        ): Data {
            val match = node.get<String>()?.let(regex::matchEntire)
            logger.debug { "Deserializing data: ${match?.groupValues?.joinToString(", ")}" }
            return when (match?.groups?.get("identifier")?.value?.lowercase()) {
                "b" -> Data(match.groups["size"]!!.value.toLong())
                "kb" -> fromKilobytes(match.groups["size"]!!.value.toLong())
                "mb" -> fromMegabytes(match.groups["size"]!!.value.toLong())
                "gb" -> fromGigabytes(match.groups["size"]!!.value.toLong())
                else -> {
                    val throwable = ConfigurateException(node, "Invalid data format")
                    logger.warn(throwable) {
                        "Unable to parse data from node: $node" +
                            "\n\t\tExpected format: <value><identifier>" +
                            "\n\t\tExpected identifiers: b, kb, mb, gb, tb" +
                            "\n\t\tReceived: ${node.get<String>()}"
                    }
                    throw throwable
                }
            }
        }

        override fun serialize(
            type: Type,
            obj: Data?,
            node: ConfigurationNode,
        ) {
            if (obj == null) { node.raw(null); return }
            when {
                obj.gigabytes > 0 -> node.set("${dropTrailingZeros(obj.gigabytes)}GB")
                obj.megabytes > 0 -> node.set("${dropTrailingZeros(obj.megabytes)}MB")
                obj.kilobytes > 0 -> node.set("${dropTrailingZeros(obj.kilobytes)}KB")
                else -> node.set("${obj.bytes}B")
            }
        }

        private fun dropTrailingZeros(value: Double): String {
            return if (value.roundToLong().toDouble() == value) {
                value.roundToLong().toString()
            } else value.toString()
        }
    }
}

fun Long.kilobytes(): Data = Data.fromKilobytes(this)
fun Long.megabytes(): Data = Data.fromMegabytes(this)
fun Long.gigabytes(): Data = Data.fromGigabytes(this)
