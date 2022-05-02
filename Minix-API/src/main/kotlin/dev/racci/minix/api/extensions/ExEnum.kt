package dev.racci.minix.api.extensions

fun Enum<*>.formatted(
    separator: String = " ",
    capitalize: Boolean = true
): String {
    val split = name.lowercase().split("_").toMutableList()
    if (capitalize) {
        split.forEachIndexed { index, s ->
            split[index] = s.replaceFirstChar { it.titlecase() }
        }
    }
    return split.joinToString(separator)
}
