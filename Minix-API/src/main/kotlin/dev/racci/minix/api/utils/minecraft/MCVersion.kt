package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.getKoin
import kotlinx.atomicfu.atomic
import kotlinx.collections.immutable.toImmutableList
import org.bukkit.Bukkit
import kotlin.reflect.KProperty

class MCVersion private constructor(
    val name: String,
    val versionID: Int,
    val protocolVersion: Int,
) : Comparable<MCVersion> {
    val ordinal = ordinalInc.getAndIncrement()

    val supportsUUIDs: Boolean = versionID > 11
    val supportsDuelWielding: Boolean = protocolVersion >= 107
    val supportsRGBColour: Boolean = protocolVersion >= 735

    val majorMinecraftVersion: MCVersion get() = valueOf(mainVersionString)

    private val identifier: String

    override operator fun compareTo(other: MCVersion): Int {
        return when {
            versionID == other.versionID -> 0
            versionID > other.versionID && other != UNKNOWN -> 1
            versionID < other.versionID && this != UNKNOWN -> -1
            else -> throw RuntimeException("Cannot compare $this and $other due to unknown version")
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MCVersion -> versionID == other.versionID
            else -> false
        }
    }

    val mainVersionString: String
    val versionString: String

    init {
        val matcher = versionRegex.matchEntire(name)
        val nms: Boolean
        if (matcher == null) {
            mainVersionString = "UNKNOWN"
            versionString = "UNKNOWN"
            nms = false
        } else {
            val major = matcher.groups["major"]!!
            val minor = matcher.groups["minor"]!!
            val patch = matcher.groups["patch"]
            nms = matcher.groups["nms"] != null
            val revision = matcher.groups["revision"]

            mainVersionString = "${major}_$minor"
            versionString = StringBuilder("$major.$minor").apply {
                if (patch != null) append(".$patch")
                if (nms) append("_NMS_$revision")
            }.toString()
        }
        identifier = mainVersionString + "_R" + versionID % 10

        if (nms) {
            nmsMap[identifier] = this
        } else {
            nameMap[name] = this
            protocolMap[protocolVersion] = this
        }
    }

    /**
     * Checks weather the given version is from the same major MC version.
     * e.g. MC 1.7.2 and MC 1.7.10 are both MC 1.7 and will result in true.
     * while MC 1.7.10 and MC 1.8.8 are MC 1.7 and MC 1.8 and will therefore result in false.
     *
     * @param other The other version to compare with
     * @return True if both are from the same major MC version. false if not.
     */
    fun isSameMajorVersion(other: MCVersion): Boolean {
        return versionID / 10 == other.versionID / 10
    }

    override fun toString(): String {
        return name
    }

    override fun hashCode(): Int = ordinal

    class MCVersionDelegate internal constructor(
        private val versionID: Int,
        private val protocolVersion: Int,
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): MCVersion {
            return MCVersion(property.name, versionID, protocolVersion)
        }
    }

    companion object {
        private val ordinalInc = atomic(0)
        private val nameMap = mutableMapOf<String, MCVersion>()
        private val protocolMap = mutableMapOf<Int, MCVersion>()
        private val nmsMap = mutableMapOf<String, MCVersion>()
        val currentVersion: MCVersion = run {
            val result = Regex("\\(MC: (?<version>\\d.\\d+(.\\d+)?)\\)").matchEntire(Bukkit.getVersion())
            var version: MCVersion = UNKNOWN
            if (result != null) {
                version = valueOf(result.groups["version"]!!.value)
            }
            if (version === UNKNOWN) {
                nmsMap.keys.find { server::class.java.name.split("\\.")[3] in it }?.let { version = valueOf(it) }
            }
            if (version === UNKNOWN) {
                getKoin().get<Minix>().log.error { "Failed to obtain a server version!" }
            }
            version
        }
        val protocolVersions: List<MCVersion> get() = protocolMap.values.toImmutableList()
        val versions: List<MCVersion> get() = nameMap.values.toImmutableList()
        val supportsUUIDs: Boolean get() = currentVersion.supportsUUIDs
        val supportsDuelWielding: Boolean get() = currentVersion.supportsDuelWielding
        val supportsRGB: Boolean get() = currentVersion.supportsRGBColour

        fun valueOf(versionName: String): MCVersion = nameMap[versionName] ?: UNKNOWN

        fun valueOf(protocolVersion: Int): MCVersion = protocolMap[protocolVersion] ?: UNKNOWN

        operator fun get(versionName: String): MCVersion = valueOf(versionName)

        operator fun get(protocolVersion: Int): MCVersion = valueOf(protocolVersion)

        fun fromName(versionName: String): MCVersion = get(versionName)

        fun fromProtocol(protocolVersion: Int): MCVersion = get(protocolVersion)

        /**
         * Checks weather the versions are from the same major MC version.
         *
         * @param other The other MCVersion
         * @return True if both are from the same major version, false if not
         */
        infix fun MCVersion.sameMajor(other: MCVersion): Boolean = this != UNKNOWN && versionID / 10 == other.versionID / 10

        private val versionRegex = Regex("MC_?(?<nms>NMS)?_(?<major>\\d+)_(?<minor>\\d+)_?(?<patch>\\d+)?_?(?<revision>R\\d)?")

        val UNKNOWN by MCVersionDelegate(0, -1)
        val MC_1_7 by MCVersionDelegate(11, 3)
        val MC_1_7_1 by MCVersionDelegate(11, 3)
        val MC_1_7_2 by MCVersionDelegate(11, 4)
        val MC_1_7_3 by MCVersionDelegate(11, 4)
        val MC_1_7_4 by MCVersionDelegate(11, 4)
        val MC_NMS_1_7_R1 by MCVersionDelegate(11, 4)
        val MC_1_7_5 by MCVersionDelegate(12, 5)
        val MC_1_7_6 by MCVersionDelegate(12, 5)
        val MC_1_7_7 by MCVersionDelegate(12, 5)
        val MC_NMS_1_7_R2 by MCVersionDelegate(12, 5)
        val MC_1_7_8 by MCVersionDelegate(13, 5)
        val MC_1_7_9 by MCVersionDelegate(13, 5)
        val MC_NMS_1_7_R3 by MCVersionDelegate(13, 5)
        val MC_1_7_10 by MCVersionDelegate(14, 5)
        val MC_NMS_1_7_R4 by MCVersionDelegate(14, 5)
        val MC_1_8 by MCVersionDelegate(21, 47)
        val MC_1_8_1 by MCVersionDelegate(21, 47)
        val MC_1_8_2 by MCVersionDelegate(21, 47)
        val MC_NMS_1_8_R1 by MCVersionDelegate(21, 47)
        val MC_1_8_3 by MCVersionDelegate(22, 47)
        val MC_1_8_4 by MCVersionDelegate(22, 47)
        val MC_1_8_5 by MCVersionDelegate(22, 47)
        val MC_1_8_6 by MCVersionDelegate(22, 47)
        val MC_1_8_7 by MCVersionDelegate(22, 47)
        val MC_NMS_1_8_R2 by MCVersionDelegate(22, 47)
        val MC_1_8_8 by MCVersionDelegate(23, 47)
        val MC_1_8_9 by MCVersionDelegate(23, 47)
        val MC_NMS_1_8_R3 by MCVersionDelegate(23, 47)
        val MC_1_9 by MCVersionDelegate(31, 107)
        val MC_1_9_1 by MCVersionDelegate(31, 108)
        val MC_1_9_2 by MCVersionDelegate(31, 109)
        val MC_NMS_1_9_R1 by MCVersionDelegate(31, 109)
        val MC_1_9_3 by MCVersionDelegate(32, 110)
        val MC_1_9_4 by MCVersionDelegate(32, 110)
        val MC_NMS_1_9_R2 by MCVersionDelegate(32, 110)
        val MC_1_10 by MCVersionDelegate(41, 210)
        val MC_1_10_1 by MCVersionDelegate(41, 210)
        val MC_1_10_2 by MCVersionDelegate(41, 210)
        val MC_NMS_1_10_R1 by MCVersionDelegate(41, 210)
        val MC_1_11 by MCVersionDelegate(51, 315)
        val MC_1_11_1 by MCVersionDelegate(51, 316)
        val MC_1_11_2 by MCVersionDelegate(51, 316)
        val MC_NMS_1_11_R1 by MCVersionDelegate(51, 316)
        val MC_1_12 by MCVersionDelegate(61, 335)
        val MC_1_12_1 by MCVersionDelegate(61, 338)
        val MC_1_12_2 by MCVersionDelegate(61, 340)
        val MC_NMS_1_12_R1 by MCVersionDelegate(61, 340)
        val MC_1_13 by MCVersionDelegate(71, 393)
        val MC_NMS_1_13_R1 by MCVersionDelegate(71, 393)
        val MC_1_13_1 by MCVersionDelegate(72, 401)
        val MC_1_13_2 by MCVersionDelegate(72, 404)
        val MC_NMS_1_13_R2 by MCVersionDelegate(72, 404)
        val MC_1_14 by MCVersionDelegate(81, 477)
        val MC_NMS_1_14_R1 by MCVersionDelegate(81, 498)
        val MC_1_14_1 by MCVersionDelegate(81, 480)
        val MC_1_14_2 by MCVersionDelegate(81, 485)
        val MC_1_14_3 by MCVersionDelegate(81, 490)
        val MC_1_14_4 by MCVersionDelegate(81, 498)
        val MC_1_15 by MCVersionDelegate(91, 573)
        val MC_1_15_1 by MCVersionDelegate(91, 575)
        val MC_1_15_2 by MCVersionDelegate(91, 578)
        val MC_NMS_1_15_R1 by MCVersionDelegate(91, 578)
        val MC_1_16 by MCVersionDelegate(101, 735)
        val MC_1_16_1 by MCVersionDelegate(101, 736)
        val MC_NMS_1_16_R1 by MCVersionDelegate(101, 736)
        val MC_1_16_2 by MCVersionDelegate(102, 751)
        val MC_1_16_3 by MCVersionDelegate(102, 753)
        val MC_NMS_1_16_R2 by MCVersionDelegate(102, 751)
        val MC_1_16_4 by MCVersionDelegate(103, 754)
        val MC_1_16_5 by MCVersionDelegate(103, 754)
        val MC_NMS_1_16_R3 by MCVersionDelegate(103, 754)
        val MC_1_17 by MCVersionDelegate(111, 755)
        val MC_1_17_1 by MCVersionDelegate(111, 756)
        val MC_NMS_1_17_R1 by MCVersionDelegate(111, 756)
        val MC_1_18 by MCVersionDelegate(121, 757)
        val MC_1_18_1 by MCVersionDelegate(121, 757)
        val MC_NMS_1_18_R1 by MCVersionDelegate(121, 757)
        val MC_1_18_2 by MCVersionDelegate(122, 758)
        val MC_NMS_1_18_R2 by MCVersionDelegate(122, 758)
        val MC_1_19 by MCVersionDelegate(131, Int.MAX_VALUE)
        val MC_NMS_1_19_R1 by MCVersionDelegate(131, Int.MAX_VALUE)
    }
}
