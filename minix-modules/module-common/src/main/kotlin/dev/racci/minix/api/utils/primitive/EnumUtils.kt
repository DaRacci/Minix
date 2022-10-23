package dev.racci.minix.api.utils.primitive

public object EnumUtils {

    /**
     * Returns `true` if enum T contains an entry with the specified name.
     */
    public inline fun <reified T : Enum<T>> enumContains(
        name: String,
        ignoreCase: Boolean
    ): Boolean = enumValues<T>().any { it.name.equals(name, ignoreCase) }

    /**
     * Attempts to get the enum by this name.
     *
     * @param name The name to get.
     * @param T The type of the enum.
     * @return The found enum or null.
     */
    public inline fun <reified T : Enum<T>> getByName(name: String?): T? {
        if (name == null) return null
        return enumValues<T>().asSequence()
            .firstOrNull { it.name == name }
    }

    public fun formatted(
        enum: Enum<*>,
        separator: String = " ",
        capitalize: Boolean = true
    ): String {
        val split = enum.name.lowercase().split("_").toMutableList()
        if (capitalize) {
            split.forEachIndexed { index, s ->
                split[index] = s.replaceFirstChar { it.titlecase() }
            }
        }

        return split.joinToString(separator)
    }
}
