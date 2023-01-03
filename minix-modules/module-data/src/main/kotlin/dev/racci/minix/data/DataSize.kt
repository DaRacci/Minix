package dev.racci.minix.data

import dev.racci.minix.data.serializers.kotlin.DataSerializer
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

@Serializable(with = DataSerializer::class)
public data class DataSize(val bytes: Long) : Comparable<DataSize> {

    val kilobytes: Double by lazy { bytes / 1024.0 }
    val megabytes: Double by lazy { bytes / 1048576.0 }
    val gigabytes: Double by lazy { bytes / 1073741824.0 }

    val kilobytesRounded: Long by lazy { bytes / 1024 }
    val megabytesRounded: Long by lazy { bytes / 1048576 }
    val gigabytesRounded: Long by lazy { bytes / 1073741824 }

    override fun compareTo(other: DataSize): Int = when {
        bytes > other.bytes -> 1
        bytes < other.bytes -> -1
        else -> 0
    }

    // TODO: There has to be a better way to do this
    public fun humanReadableSize(): String {
        return when {
            gigabytes > 0 -> "${gigabytes}GB"
            megabytes > 0 -> "${megabytes}MB"
            kilobytes > 0 -> "${kilobytes}KB"
            else -> "${bytes}B"
        }
    }

    override fun toString(): String = "${bytes}B"

    override fun hashCode(): Int = bytes.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is DataSize) {
            return bytes == other.bytes
        }
        return false
    }

    public companion object : KoinComponent {
        internal val regex = Regex("(?<size>\\d+(\\.)?(\\d+)?)(?<identifier>[a-zA-Z]+)")

        public fun fromKilobytes(kilobytes: Long): DataSize = DataSize(kilobytes * 1024)
        public fun fromMegabytes(megabytes: Long): DataSize = DataSize(megabytes * 1048576)
        public fun fromGigabytes(gigabytes: Long): DataSize = DataSize(gigabytes * 1073741824)
    }
}

public fun Long.kilobytes(): DataSize = DataSize.fromKilobytes(this)
public fun Long.megabytes(): DataSize = DataSize.fromMegabytes(this)
public fun Long.gigabytes(): DataSize = DataSize.fromGigabytes(this)
