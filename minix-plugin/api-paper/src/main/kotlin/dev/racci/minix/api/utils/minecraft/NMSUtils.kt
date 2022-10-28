package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.extensions.currentVersion
import kotlin.reflect.KClass

public object NMSUtils {
    public val NMS_PREFIX: String = "org.bukkit.craftbukkit.${MCVersion.currentVersion.identifier}"

    /** Gets the nms class with the current version. */
    public fun getNMSClass(name: String): KClass<*> {
        return runCatching { Class.forName("$NMS_PREFIX.$name").kotlin }.onFailure {
            throw RuntimeException("Failed to get NMS class $name", it)
        }.getOrThrow()
    }
}
