package dev.racci.minix.api.updater

import dev.racci.minix.api.plugin.MinixLogger
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import java.util.concurrent.ConcurrentHashMap

class Version @Throws(InvalidVersionStringException::class) constructor(
    rawVersion: String,
    ignoreTags: Boolean = false
) : Comparable<Version> {

    private val rawVersion: String
    private val tags: Array<String>
    private val version: IntArray
    private val timestamp: Long // TODO: use a Date object instead
    private val buildNumber: Long

    var isPreRelease = false
    val major: Int get() = version.getOrElse(0) { 0 }
    val minor: Int get() = version.getOrElse(1) { 0 }
    val patch: Int get() = version.getOrElse(2) { 0 }

    /**
     * Returns the original version string. But without the optional "v" at the start.
     *
     * @return The string representing this version.
     */
    override fun toString(): String = rawVersion

    /**
     * Checks if this version is equal to the given one.
     * If given a string it will try to parse it as a version.
     *
     * @param other The other version to compare with.
     * @return True if the versions are equal, false otherwise.
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
        val c = version.size.coerceAtMost(other.version.size)

        repeat(c) {
            when { // Compare each digit and find a difference
                other.version[it] > version[it] -> return -1
                version[it] > other.version[it] -> return 1
            }
        }

        if (version.size != other.version.size) { // If both version are the same for the length, the version that has more digits (>0) probably is the newer one.
            val otherLonger = other.version.size > version.size
            val longer = if (otherLonger) other.version else version

            for (i in c until longer.size) {
                if (longer[i] <= 0) continue
                return if (otherLonger) 1 else 0 // Return newer if other is longer
            }
        }

        (timestamp > 0 && other.timestamp > 0).ifTrue { // If both versions have the same length we still can compare the build timestamp (if available)
            return when { // Return newer if the timestamp is larger
                other.timestamp > timestamp -> -1
                other.timestamp < timestamp -> 1
                else -> return@ifTrue
            }
        }

        (buildNumber > 0 && other.buildNumber > 0).ifTrue { // If both versions still can't be distinguished we can use the build number (if available)
            return when { // Return newer if the build number is larger
                other.buildNumber > buildNumber -> 1
                other.buildNumber < buildNumber -> -1
                else -> return@ifTrue
            }
        }

        return 0 // They are both the same
    }

    @Throws(InvalidVersionStringException::class)
    operator fun compareTo(other: String): Int = compareTo(Version(other))

    fun toInt(): Int = major and 0xFF shl 22 or (minor and 0x7FF shl 12) or (patch and 0xFF) // TODO: Learn how this work

    /**
     * This exception is thrown when the string representing a version is invalid.
     */
    class InvalidVersionStringException(
        string: String? = "The version string must be in the format: ${versionStringRegex.pattern}"
    ) : IllegalArgumentException(string)

    constructor(majorVersion: Int) : this(majorVersion.toString())

    constructor(
        majorVersion: Int,
        minorVersion: Int
    ) : this("$majorVersion.$minorVersion")

    constructor(
        majorVersion: Int,
        minorVersion: Int,
        patchVersion: Int
    ) : this("$majorVersion.$minorVersion.$patchVersion")

    init {
        try {
            val matcher = versionStringRegex.matchEntire(rawVersion) ?: throw InvalidVersionStringException()
            val version = matcher.groups["version"]!!.value.replace(unimportantVersionRegex, "")

            val tags = matcher.groups["tags"]!!.value.split("-").toTypedArray()
            val comps = version.split(".").toTypedArray()
            val tagsList = if (ignoreTags) emptyList() else getAll(tags, preReleaseTags)
            val notFinalVersion = tagsList.isNotEmpty()

            this.rawVersion = rawVersion.takeUnless { it.startsWith("v", true) } ?: rawVersion.substring(1)
            this.version = IntArray(comps.size.takeUnless { notFinalVersion } ?: (comps.size + 1))
            this.buildNumber = getBuildParameter(tags, "(b|build(number)?)")
            this.timestamp = getBuildParameter(tags, "(t|ts|time(stamp)?)")
            this.tags = tags

            getKoin().get<MinixLogger>().info {
                """
                    | Raw version: $rawVersion
                    | Version: $version
                    | Tags: ${tags.joinToString(", ")}
                    | Build number: $buildNumber
                    | Timestamp: $timestamp
                """.trimIndent()
            }

            comps.indices.forEach { this.version[it] = comps[it].toInt() }

            if (notFinalVersion) {
                isPreRelease = true
                var last = 0
                for (string in tagsList) {
                    if (last == 0) last = Int.MAX_VALUE
                    var tagNumber = 0
                    var tag = string.lowercase()
                    preReleaseTagRegex.find(tag).invokeIfNotNull { result ->
                        tagNumber = result.groups["number"]!!.value.toInt()
                        tag = result.groups["tag"]!!.value
                    }
                    last = last - preReleaseTagResolution[tag]!! + tagNumber
                }
                this.version[(version.lastIndex - 1).coerceAtLeast(0)] = last
                if (last > 0) {
                    for (i in this.version.size - 2 downTo 0) {
                        if (this.version[i] > 0 || i == 0) {
                            this.version[i]--
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) { throw InvalidVersionStringException(e.message) }
    }

    companion object {

        val unimportantVersionRegex by lazy { Regex("(\\.0)*$") }
        val preReleaseTagResolution: MutableMap<String, Int> = ConcurrentHashMap()
        val preReleaseTagRegex by lazy { Regex("(?<tag>\\w+)\\.?(?<number>\\d+)") }
        val preReleaseTags by lazy { arrayOf("alpha", "a", "beta", "b", "pre", "rc", "snapshot") }
        val versionStringRegex by lazy { Regex("[vV]?(?<version>\\d+(\\.\\d+)*)-?(?<tags>([^-\\s]+)*)") }

        fun getBuildParameter(
            tags: Array<String>,
            parameter: String
        ): Long {
            val searchPattern = Regex("$parameter[=:_]?(?<number>\\d+)", RegexOption.IGNORE_CASE)
            for (tag in tags) {
                val matcher = searchPattern.find(tag) ?: continue
                try {
                    return matcher.groups["number"]?.value?.toLong() ?: continue
                } catch (ignored: NumberFormatException) { }
            }
            return -1
        }

        fun getAll(
            source: Array<String>,
            searchForArray: Array<String>
        ): ArrayList<String> {
            val result = ArrayList<String>()
            for (searchFor in searchForArray) {
                for (string in source) {
                    if (!string.contains(searchFor, true)) continue
                    result += string
                }
            }
            return result
        }

        init {
            repeat(preReleaseTags.size) {
                preReleaseTagResolution[preReleaseTags[it]] = (preReleaseTags.size + 1 - it) * 10
            }
        }
    }
}
