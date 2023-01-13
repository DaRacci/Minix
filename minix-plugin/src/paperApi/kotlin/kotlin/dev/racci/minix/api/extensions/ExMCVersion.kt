package dev.racci.minix.api.extensions

import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.MCVersion
import org.bukkit.Bukkit

public val MCVersion.Companion.currentVersion: MCVersion by lazy {
    val result = Regex("\\(MC: (?<version>\\d(.\\d{1,5}){1,2})\\)\$").find(Bukkit.getVersion())
    var version: MCVersion = MCVersion.UNKNOWN

    if (result != null) {
        val versionString = result.groups["version"]!!.value
            .replace(".", "_")
            .let { "MC_$it" }
        version = MCVersion.valueOf(versionString)
    }

    if (version === MCVersion.UNKNOWN) {
        val fqn = server::class.qualifiedName!!.split('.')[3]
        MCVersion.NMS_MAP.keys.find { fqn in it }?.let { version = MCVersion.valueOf(it) }
    }

    if (version === MCVersion.UNKNOWN) {
        getKoin().get<MinixLogger>().error { "Failed to obtain a server version!" }
    }
    version
}

public val MCVersion.Companion.supportsUUIDs: Boolean get() = currentVersion.supportsUUIDs
public val MCVersion.Companion.supportsDuelWielding: Boolean get() = currentVersion.supportsDuelWielding
public val MCVersion.Companion.supportsRGB: Boolean get() = currentVersion.supportsRGBColour
