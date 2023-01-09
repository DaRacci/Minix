package dev.racci.minix.data

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import dev.racci.minix.data.exceptions.InvalidVersionStringException
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.Instant
import org.jetbrains.annotations.Range

// TODO: Allow custom regex parsing for those fucking people who don't follow semver
public data class Version internal constructor(
    public val major: Int,
    public val minor: Int,
    public val patch: Int,
    public val tags: ImmutableSet<PreReleaseTag>,
    public val buildNumber: Option<Long>,
    public val timestamp: Option<Instant>
) : Comparable<Version> {

    public val isPreRelease: Boolean
        get() = tags.isNotEmpty()

    /**
     * Returns the original version string. But without the optional “v” at the start.
     *
     * @return The string representing this version.
     */
    override fun toString(): String = buildString {
        append('v').append(major).append('.').append(minor).append('.').append(patch)
        append(" - {")
        append("Timestamp=").append(timestamp.map { it.toString() }.getOrElse { "null" })
        append(", Build=").append(buildNumber.map { it.toString() }.getOrElse { "null" })
        append(", Tags=[").append(tags.joinToString(", ") { it.toString() }).append(']')
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other::class != Version::class) return false
        return compareTo(other as Version) == 0
    }

    override fun hashCode(): Int {
        var result = tags.hashCode()
        result = 31 * result + major.hashCode()
        result = 31 * result + minor.hashCode()
        result = 31 * result + patch.hashCode()
        result = 31 * result + buildNumber.hashCode()
        result = 31 * result + timestamp.hashCode()
        return 31 * result + tags.toTypedArray().contentHashCode()
    }

    override operator fun compareTo(other: Version): Int {
        TODO("Implement this")

//        if (other === ERROR) return 1
//        if (this === ERROR) return -1
//
//        val c = version.size.coerceAtMost(other.version.size)
//
//        repeat(c) {
//            when { // Compare each digit and find a difference
//                other.version[it] > version[it] -> return -1
//                version[it] > other.version[it] -> return 1
//            }
//        }
//
//        when {
//            isPreRelease && !other.isPreRelease -> return -1
//            !isPreRelease && other.isPreRelease -> return 1
//        }
//
//        if (version.size != other.version.size) { // If both versions are the different length, the version that has more digits (>0) probably is the newer one.
//            val otherLonger = other.version.size > version.size
//            val longer = if (otherLonger) other.version else version
//
//            for (i in c until longer.size) {
//                if (longer[i] <= 0) continue
//                return if (otherLonger) 1 else 0 // Return newer if other is longer
//            }
//        }
//
//        if (timestamp > 0 && other.timestamp > 0) {
//            return timestamp.compareTo(other.timestamp)
//        }
//
//        (buildNumber > 0 && other.buildNumber > 0).ifTrue { // If both versions still can't be distinguished, we can use the build number (if available)
//            return buildNumber.compareTo(other.buildNumber)
//        }
//
//        return 0 // They are both the same
    }

    public fun toInt(): Int = major and 0xFF shl 22 or (minor and 0x7FF shl 12) or (patch and 0xFF) // TODO: Learn how this work

    public companion object {
        private const val sep = "[=:_\\s.\\-]?"
        private val majorMinorPatch = Regex("^[vV]?(?<M>\\d+)(?:\\.(<?m>\\d+))?(?:\\.(?<p>\\d+))?")
        private val buildNumber = regex("build")
        private val timestamp = regex("timestamp", "t", "ts", "time", "date")

        private fun regex(vararg matches: String) = Regex("$sep${matches.joinToString("|")}$sep(?<value>\\d*)")

        public val EMPTY: Version by lazy { of(0, 0, 0) }

        @JvmOverloads
        public fun of(
            major: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
            minor: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
            patch: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int,
            tags: Collection<PreReleaseTag> = emptyList(),
            buildNumber: Long? = null,
            timestamp: Instant? = null
        ): Version = Version(major, minor, patch, tags.toImmutableSet(), buildNumber.toOption(), timestamp.toOption())

        public fun parseString(rawVersion: String): Version {
            var reducingString = rawVersion
            val (major, minor, patch) = with(majorMinorPatch.find(reducingString) ?: throw InvalidVersionStringException(rawVersion)) {
                fun group(group: String) = groups[group]?.value?.toInt() ?: 0
                reducingString = reducingString.drop(value.length)
                arrayOf(group("M"), group("m"), group("p"))
            }

            fun getTag(
                regex: Regex,
                def: Long?
            ): Long? = regex.find(reducingString)?.let { match ->
                val startingIndex = reducingString.indexOf(match.value)
                reducingString = reducingString.filterIndexed { index, _ -> index !in startingIndex until match.value.length }
                match.groups["value"]?.value?.toLongOrNull() ?: def
            }

            val buildNumber = getTag(buildNumber, null)
            val timestamp = getTag(timestamp, null)
            val tags = PreReleaseTag.Type.values().mapNotNull { type ->
                PreReleaseTag(type, getTag(type.regex, 0)?.toInt() ?: return@mapNotNull null)
            }

            return of(
                major,
                minor,
                patch,
                tags,
                buildNumber,
                timestamp?.let(Instant::fromEpochMilliseconds)
            )
        }
    }

    public class PreReleaseTag internal constructor(
        type: Type,
        value: Int
    ) {
        // Type is sorted by ordinal, so we can use the ordinal as the priority, then the value ontop of that.
        public val level: Int = (type.ordinal * 1000) + value

        // TODO: Add support for custom tags
        public enum class Type(vararg abbreviations: String = emptyArray()) {
            ALPHA("a"),
            BETA("b"),
            RELEASE_CANDIDATE("rc"),
            SNAPSHOT("pre", "dev", "nightly");

            public val regex: Regex = buildString {
                append("(?:")
                abbreviations.forEachIndexed { index, s ->
                    append(s)
                    if (index != abbreviations.lastIndex) append("|")
                }
                append(")")
                append("(?:[.\\-]?(?<value>\\d+))?")
            }.toRegex(RegexOption.IGNORE_CASE)
        }
    }
}
