package dev.racci.minix.api.utils.minecraft

import kotlinx.atomicfu.atomic
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.typeOf

public class MCVersion private constructor(
    public val name: String,
    public val versionID: Int,
    public val protocolVersion: Int
) : Comparable<MCVersion> {
    private val isNMS: Boolean

    public val identifier: String
    public val ordinal: Int = ordinalInc.getAndIncrement()
    public val mainVersionString: String
    public val versionString: String
    public val supportsUUIDs: Boolean = versionID > 11
    public val supportsDuelWielding: Boolean = protocolVersion >= 107
    public val supportsRGBColour: Boolean = protocolVersion >= 735
    public val majorMinecraftVersion: MCVersion get() = valueOf(mainVersionString)

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

    init {
        val matcher = versionRegex.matchEntire(name)
        if (matcher == null) {
            mainVersionString = "UNKNOWN"
            versionString = "UNKNOWN"
            isNMS = false
        } else {
            val major = matcher.groups["major"]!!
            val minor = matcher.groups["minor"]!!
            val patch = matcher.groups["patch"]
            isNMS = matcher.groups["nms"] != null
            val revision = matcher.groups["revision"]

            mainVersionString = "${major}_$minor"
            versionString = StringBuilder("$major.$minor").apply {
                if (patch != null) append(".$patch")
                if (isNMS) append("_NMS_$revision")
            }.toString()
        }
        identifier = mainVersionString + "_R" + versionID % 10
    }

    /**
     * Checks weather the given version is from the same major MC version.
     * e.g. MC 1.7.2 and MC 1.7.10 are both MC 1.7 and will result in true.
     * while MC 1.7.10 and MC 1.8.8 are MC 1.7 and MC 1.8 and will therefore result in false.
     *
     * @param other The other version to compare with
     * @return True if both are from the same major MC version. false if not.
     */
    public fun isSameMajorVersion(other: MCVersion): Boolean {
        return versionID / 10 == other.versionID / 10
    }

    override fun toString(): String {
        return name
    }

    override fun hashCode(): Int = ordinal

    private class MCVersionDelegate(
        private val versionID: Int,
        private val protocolVersion: Int
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): MCVersion {
            return MCVersion(property.name, versionID, protocolVersion)
        }
    }

    @Suppress("UNUSED")
    public companion object {
        private val ordinalInc = atomic(0)
        public val NAME_MAP: ImmutableMap<String, MCVersion>
        public val PROTOCOL_MAP: ImmutableMap<Int, MCVersion>
        public val NMS_MAP: ImmutableMap<String, MCVersion>

        public val protocolVersions: List<MCVersion> get() = PROTOCOL_MAP.values.toImmutableList()
        public val versions: List<MCVersion> get() = NAME_MAP.values.toImmutableList()

        public fun valueOf(versionName: String): MCVersion = NAME_MAP[versionName] ?: UNKNOWN

        public fun valueOf(protocolVersion: Int): MCVersion = PROTOCOL_MAP[protocolVersion] ?: UNKNOWN

        public operator fun get(versionName: String): MCVersion = valueOf(versionName)

        public operator fun get(protocolVersion: Int): MCVersion = valueOf(protocolVersion)

        public fun fromName(versionName: String): MCVersion = get(versionName)

        public fun fromProtocol(protocolVersion: Int): MCVersion = get(protocolVersion)

        /**
         * Checks weather the versions are from the same major MC version.
         *
         * @param other The other MCVersion
         * @return True if both are from the same major version, false if not
         */
        public infix fun MCVersion.sameMajor(other: MCVersion): Boolean = this != UNKNOWN && (versionID / 10 == other.versionID / 10)

        private val versionRegex = Regex("MC_?(?<nms>NMS)?_(?<major>\\d+)_(?<minor>\\d+)_?(?<patch>\\d+)?_?(?<revision>R\\d)?")

        public val UNKNOWN: MCVersion by MCVersionDelegate(0, -1)
        public val MC_1_7: MCVersion by MCVersionDelegate(11, 3)
        public val MC_1_7_1: MCVersion by MCVersionDelegate(11, 3)
        public val MC_1_7_2: MCVersion by MCVersionDelegate(11, 4)
        public val MC_1_7_3: MCVersion by MCVersionDelegate(11, 4)
        public val MC_1_7_4: MCVersion by MCVersionDelegate(11, 4)
        public val MC_NMS_1_7_R1: MCVersion by MCVersionDelegate(11, 4)
        public val MC_1_7_5: MCVersion by MCVersionDelegate(12, 5)
        public val MC_1_7_6: MCVersion by MCVersionDelegate(12, 5)
        public val MC_1_7_7: MCVersion by MCVersionDelegate(12, 5)
        public val MC_NMS_1_7_R2: MCVersion by MCVersionDelegate(12, 5)
        public val MC_1_7_8: MCVersion by MCVersionDelegate(13, 5)
        public val MC_1_7_9: MCVersion by MCVersionDelegate(13, 5)
        public val MC_NMS_1_7_R3: MCVersion by MCVersionDelegate(13, 5)
        public val MC_1_7_10: MCVersion by MCVersionDelegate(14, 5)
        public val MC_NMS_1_7_R4: MCVersion by MCVersionDelegate(14, 5)
        public val MC_1_8: MCVersion by MCVersionDelegate(21, 47)
        public val MC_1_8_1: MCVersion by MCVersionDelegate(21, 47)
        public val MC_1_8_2: MCVersion by MCVersionDelegate(21, 47)
        public val MC_NMS_1_8_R1: MCVersion by MCVersionDelegate(21, 47)
        public val MC_1_8_3: MCVersion by MCVersionDelegate(22, 47)
        public val MC_1_8_4: MCVersion by MCVersionDelegate(22, 47)
        public val MC_1_8_5: MCVersion by MCVersionDelegate(22, 47)
        public val MC_1_8_6: MCVersion by MCVersionDelegate(22, 47)
        public val MC_1_8_7: MCVersion by MCVersionDelegate(22, 47)
        public val MC_NMS_1_8_R2: MCVersion by MCVersionDelegate(22, 47)
        public val MC_1_8_8: MCVersion by MCVersionDelegate(23, 47)
        public val MC_1_8_9: MCVersion by MCVersionDelegate(23, 47)
        public val MC_NMS_1_8_R3: MCVersion by MCVersionDelegate(23, 47)
        public val MC_1_9: MCVersion by MCVersionDelegate(31, 107)
        public val MC_1_9_1: MCVersion by MCVersionDelegate(31, 108)
        public val MC_1_9_2: MCVersion by MCVersionDelegate(31, 109)
        public val MC_NMS_1_9_R1: MCVersion by MCVersionDelegate(31, 109)
        public val MC_1_9_3: MCVersion by MCVersionDelegate(32, 110)
        public val MC_1_9_4: MCVersion by MCVersionDelegate(32, 110)
        public val MC_NMS_1_9_R2: MCVersion by MCVersionDelegate(32, 110)
        public val MC_1_10: MCVersion by MCVersionDelegate(41, 210)
        public val MC_1_10_1: MCVersion by MCVersionDelegate(41, 210)
        public val MC_1_10_2: MCVersion by MCVersionDelegate(41, 210)
        public val MC_NMS_1_10_R1: MCVersion by MCVersionDelegate(41, 210)
        public val MC_1_11: MCVersion by MCVersionDelegate(51, 315)
        public val MC_1_11_1: MCVersion by MCVersionDelegate(51, 316)
        public val MC_1_11_2: MCVersion by MCVersionDelegate(51, 316)
        public val MC_NMS_1_11_R1: MCVersion by MCVersionDelegate(51, 316)
        public val MC_1_12: MCVersion by MCVersionDelegate(61, 335)
        public val MC_1_12_1: MCVersion by MCVersionDelegate(61, 338)
        public val MC_1_12_2: MCVersion by MCVersionDelegate(61, 340)
        public val MC_NMS_1_12_R1: MCVersion by MCVersionDelegate(61, 340)
        public val MC_1_13: MCVersion by MCVersionDelegate(71, 393)
        public val MC_NMS_1_13_R1: MCVersion by MCVersionDelegate(71, 393)
        public val MC_1_13_1: MCVersion by MCVersionDelegate(72, 401)
        public val MC_1_13_2: MCVersion by MCVersionDelegate(72, 404)
        public val MC_NMS_1_13_R2: MCVersion by MCVersionDelegate(72, 404)
        public val MC_1_14: MCVersion by MCVersionDelegate(81, 477)
        public val MC_NMS_1_14_R1: MCVersion by MCVersionDelegate(81, 498)
        public val MC_1_14_1: MCVersion by MCVersionDelegate(81, 480)
        public val MC_1_14_2: MCVersion by MCVersionDelegate(81, 485)
        public val MC_1_14_3: MCVersion by MCVersionDelegate(81, 490)
        public val MC_1_14_4: MCVersion by MCVersionDelegate(81, 498)
        public val MC_1_15: MCVersion by MCVersionDelegate(91, 573)
        public val MC_1_15_1: MCVersion by MCVersionDelegate(91, 575)
        public val MC_1_15_2: MCVersion by MCVersionDelegate(91, 578)
        public val MC_NMS_1_15_R1: MCVersion by MCVersionDelegate(91, 578)
        public val MC_1_16: MCVersion by MCVersionDelegate(101, 735)
        public val MC_1_16_1: MCVersion by MCVersionDelegate(101, 736)
        public val MC_NMS_1_16_R1: MCVersion by MCVersionDelegate(101, 736)
        public val MC_1_16_2: MCVersion by MCVersionDelegate(102, 751)
        public val MC_1_16_3: MCVersion by MCVersionDelegate(102, 753)
        public val MC_NMS_1_16_R2: MCVersion by MCVersionDelegate(102, 751)
        public val MC_1_16_4: MCVersion by MCVersionDelegate(103, 754)
        public val MC_1_16_5: MCVersion by MCVersionDelegate(103, 754)
        public val MC_NMS_1_16_R3: MCVersion by MCVersionDelegate(103, 754)
        public val MC_1_17: MCVersion by MCVersionDelegate(111, 755)
        public val MC_1_17_1: MCVersion by MCVersionDelegate(111, 756)
        public val MC_NMS_1_17_R1: MCVersion by MCVersionDelegate(111, 756)
        public val MC_1_18: MCVersion by MCVersionDelegate(121, 757)
        public val MC_1_18_1: MCVersion by MCVersionDelegate(121, 757)
        public val MC_NMS_1_18_R1: MCVersion by MCVersionDelegate(121, 757)
        public val MC_1_18_2: MCVersion by MCVersionDelegate(122, 758)
        public val MC_NMS_1_18_R2: MCVersion by MCVersionDelegate(122, 758)
        public val MC_1_19: MCVersion by MCVersionDelegate(131, 759)
        public val MC_NMS_1_19_R1: MCVersion by MCVersionDelegate(131, 759)
        public val MC_1_19_1: MCVersion by MCVersionDelegate(131, 760)
        public val MC_1_19_2: MCVersion by MCVersionDelegate(131, 760)
        public val MC_NMS_1_19_R2: MCVersion by MCVersionDelegate(131, 760)
        public val MC_1_19_3: MCVersion by MCVersionDelegate(131, 761)

        init {
            val nameMap = mutableMapOf<String, MCVersion>()
            val protocolMap = mutableMapOf<Int, MCVersion>()
            val nmsMap = mutableMapOf<String, MCVersion>()

            this::class.declaredMemberProperties
                .filter { it.returnType == typeOf<MCVersion>() }
                .onEach { println(it.name) }
                .map { it.getter.call(this) as MCVersion } // Creates instances on boot
                .onEach { version ->
                    if (version.isNMS) {
                        nmsMap[version.identifier] = version
                    } else {
                        nameMap[version.name] = version
                        protocolMap[version.protocolVersion] = version
                    }
                }

            NAME_MAP = nameMap.toImmutableMap()
            PROTOCOL_MAP = protocolMap.toImmutableMap()
            NMS_MAP = nmsMap.toImmutableMap()
        }
    }
}
