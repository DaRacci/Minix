package dev.racci.minix.api.utils.primitive

import dev.racci.minix.api.utils.UtilObject

object EnumUtils : UtilObject by UtilObject {

    /**
     * Returns `true` if enum T contains an entry with the specified name.
     */
    inline fun <reified T : Enum<T>> enumContains(name: String): Boolean = enumValues<T>().any { it.name == name }

    inline fun <reified T : Enum<T>> getByName(name: String?): T? {
        if (name == null) return null
        return enumValues<T>().asSequence()
            .firstOrNull { it.name == name }
    }
}
