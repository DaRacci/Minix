package dev.racci.minix.api.utils.primitive

private val unicodeRegex = "((\\\\u)(\\d{4}))".toRegex()

fun String.javaUnicodeToCharacter(): String = unicodeRegex.replace(this) {
    String(charArrayOf(it.destructured.component3().toInt(16).toChar()))
}

fun String.centralize(
    length: Int,
    spacer: String = " ",
    prefix: String = "",
    suffix: String = ""
): String {
    if (this.length >= length) return this
    val part = prefix + spacer.repeat((length - this.length) / 2) + suffix
    return part + this + part
}
