package dev.racci.minix.data

import dev.racci.minix.api.extensions.collections.contains
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.data.exceptions.InvalidVersionStringException
import org.intellij.lang.annotations.Language
import java.util.concurrent.ConcurrentHashMap

public class Version @Throws(InvalidVersionStringException::class)
constructor(
    rawVersion: String,
    ignoreTags: Boolean = false
) : Comparable<Version> {

    private val tags: List<String>
    private val version: IntArray
    public val rawVersion: String
    public val timestamp: Long // TODO: use a Date object instead
    public val buildNumber: Long

    public var isPreRelease: Boolean = false
    public val major: Int get() = version.getOrElse(0) { 0 }
    public val minor: Int get() = version.getOrElse(1) { 0 }
    public val patch: Int get() = version.getOrElse(2) { 0 }

    public constructor(
        majorVersion: Int
    ) : this(majorVersion.toString())

    public constructor(
        majorVersion: Int,
        minorVersion: Int
    ) : this("$majorVersion.$minorVersion")

    public constructor(
        majorVersion: Int,
        minorVersion: Int,
        patchVersion: Int
    ) : this("$majorVersion.$minorVersion.$patchVersion")

    /**
     * Returns the original version string. But without the optional “v” at the start.
     *
     * @return The string representing this version.
     */
    override fun toString(): String = "v$rawVersion (Tags: [ ${tags.joinToString(", ")} ], Timestamp: $timestamp, Build: $buildNumber, PreRelease: $isPreRelease, Major: $major, Minor: $minor, Patch: $patch)"

    /**
     * Checks if this version is equal to the given one.
     * If given a string it will try to parse it as a version.
     *
     * @param other The other version to compare with.
     * @return If the versions are equal.
     * @throws InvalidVersionStringException If the given string is not a valid version string.
     */
    @Throws(InvalidVersionStringException::class)
    override fun equals(other: Any?): Boolean = when (other) {
        is Version -> compareTo(other) == 0
        is String -> compareTo(Version(other)) == 0
        else -> false
    }

    override fun hashCode(): Int = version.hashCode()

    override operator fun compareTo(other: Version): Int {
        if (other === ERROR) return 1
        if (this === ERROR) return -1

        val c = version.size.coerceAtMost(other.version.size)

        repeat(c) {
            when { // Compare each digit and find a difference
                other.version[it] > version[it] -> return -1
                version[it] > other.version[it] -> return 1
            }
        }

        when {
            isPreRelease && !other.isPreRelease -> return -1
            !isPreRelease && other.isPreRelease -> return 1
        }

        if (version.size != other.version.size) { // If both versions are the different length, the version that has more digits (>0) probably is the newer one.
            val otherLonger = other.version.size > version.size
            val longer = if (otherLonger) other.version else version

            for (i in c until longer.size) {
                if (longer[i] <= 0) continue
                return if (otherLonger) 1 else 0 // Return newer if other is longer
            }
        }

        if (timestamp > 0 && other.timestamp > 0) {
            return timestamp.compareTo(other.timestamp)
        }

        (buildNumber > 0 && other.buildNumber > 0).ifTrue { // If both versions still can't be distinguished, we can use the build number (if available)
            return buildNumber.compareTo(other.buildNumber)
        }

        return 0 // They are both the same
    }

    @Throws(InvalidVersionStringException::class)
    public operator fun compareTo(other: String): Int = compareTo(Version(other))

    public fun toInt(): Int = major and 0xFF shl 22 or (minor and 0x7FF shl 12) or (patch and 0xFF) // TODO: Learn how this work

    init {
        if (rawVersion == "ERROR") {
            this.rawVersion = rawVersion
            this.version = IntArray(3) { 0 }
            this.buildNumber = -1
            this.timestamp = -1
            this.tags = emptyList()
        } else {
            val (version, length) = cutUnneeded(rawVersion)
            val comps = getSplitVersion(version)
            val tags = getTags(rawVersion, length)
            val groupTagValues = getGroupTagValues(tags)

            this.buildNumber = getBuildParameter(tags, "(^(\\s|build|b)?)")
            this.timestamp = getBuildParameter(tags, "(t|ts|time(stamp)?)")

            // Only retain the valid snapshot tags or if ignoring tags keep none.
            this.tags = tags.filter { !ignoreTags && it !in groupTagValues && preReleaseTags.contains(it, true) }
            this.isPreRelease = this.tags.isNotEmpty()
            this.rawVersion = rawVersion.dropWhile { !it.isDigit() }

            // Convert string to an int array, if pre-release add an extra -1 to be defined later.
            val size = if (this.tags.isEmpty()) comps.size else comps.size + 1
            this.version = IntArray(size) { comps.getOrNull(it)?.toInt() ?: 0 }

            doBlackMagic()
        }
    }

    /** Returns the string paired to its total length in characters from dropping unneeded beforehand. */
    private fun cutUnneeded(rawVersion: String): Pair<String, Int> {
        var length = 0
        return rawVersion
            .dropWhile {
                if (!it.isDigit()) {
                    length++
                    true
                } else false
            }
            .takeWhile {
                if (it.isDigit() || it == '.') {
                    length++
                    true
                } else false
            } to length
    }

    private fun getSplitVersion(version: String): List<String> {
        val split = version.split(".")
        return when (split.size) {
            0 -> listOf(version, "0", "0")
            in 1..3 -> {
                val mut = split.toMutableList()
                while (mut.size < 3) mut.add("0")
                mut
            }
            else -> split
        }
    }

    private fun getTags(
        rawVersion: String,
        startingIndex: Int
    ) = tagRegex.findAll(rawVersion.substring(startingIndex))
        .map { it.groups["grouped"]?.value?.replace("\\s|-".toRegex(), "") ?: it.value }
        .toList()

    private fun getGroupTagValues(tags: List<String>): List<String> {
        val grouped by lazy { arrayOfNulls<String>(tags.size) } // This may be unused.
        for (i in tags.indices) {
            if (tags.lastIndex == i) break

            val tag = tags[i]
            val nextTag = tags[i + 1]

            if (!groupTagRegex.matches(tag)) continue
            if (!nextTag.any(Char::isDigit)) continue

            grouped[i] = tag
            grouped[i + 1] = nextTag
        }
        return grouped.filterNotNull()
    }

    private fun doBlackMagic() {
        if (!isPreRelease) return
        var last = 0
        for (tagStr in this.tags) {
            if (last == 0) last = Int.MAX_VALUE

            var tagNumber = 0
            var tag = tagStr.lowercase()

            preReleaseTagRegex.find(tag)?.apply {
                tagNumber = groups["number"]?.value?.toInt() ?: 0
                tag = groups["tag"]?.value?.lowercase() ?: ""
            }

            last -= (preReleaseTagResolution[tag]!! + tagNumber)
        }

        this.version[this.version.lastIndex] = last

        if (last < 1) return

        // I'll be honest, I don't know why this works. Hence, the method name.
        for (i in this.version.lastIndex downTo 0) {
            if (this.version[i] > 0 || i == 0) {
                this.version[i]--
                break
            }
        }
    }

    public companion object {

        public val ERROR: Version by lazy { Version("ERROR") }
        public val tagRegex: Regex by lazy { Regex("([^-\\s()]+)|\\((?<grouped>.+)\\)") }
        public val groupTagRegex: Regex by lazy { Regex("b|build|t|ts|time|d|date", RegexOption.IGNORE_CASE) }
        public val preReleaseTagResolution: MutableMap<String, Int> = ConcurrentHashMap()
        public val preReleaseTagRegex: Regex by lazy { Regex("(?<tag>\\w+)\\.?(?<number>\\d+)") }
        public val preReleaseTags: Array<String> by lazy { arrayOf("alpha", "a", "beta", "b", "pre", "rc", "snapshot") }
        public val versionStringRegex: Regex by lazy { Regex("[vV]?(?<version>\\d+(\\.\\d+)*)-?(?<tags>([^-\\s]+[\\s-]*)*)") }

        public fun getBuildParameter(
            tags: List<String>,
            @Language("RegExp") parameter: String
        ): Long {
            val searchPattern = Regex("$parameter[=:_\\s]?(?<number>\\d+)", RegexOption.IGNORE_CASE)
            for (tag in tags) {
                val matcher = searchPattern.find(tag) ?: continue
                try {
                    return matcher.groups["number"]?.value?.toLong() ?: continue
                } catch (ignored: NumberFormatException) { /* Ignored */ }
            }
            return -1
        }

        init {
            repeat(preReleaseTags.size) {
                preReleaseTagResolution[preReleaseTags[it]] = (preReleaseTags.size + 1 - it) * 10
            }
        }
    }
}
