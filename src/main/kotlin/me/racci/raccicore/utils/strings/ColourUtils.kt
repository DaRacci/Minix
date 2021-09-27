package me.racci.raccicore.utils.strings

import net.md_5.bungee.api.ChatColor
import java.util.regex.Matcher
import java.util.regex.Pattern


private val hexPattern: Pattern = Pattern.compile("(#[A-Fa-f0-9]{6})")

/**
 * @param [string] Text to translate
 * @param [parseHex] Weather to translate hex or not, Defaults to true
 * @return Returns the translated [String] with [parseHex] for translating hex
 */
fun colour(string: String?, parseHex: Boolean = true): String? {
    return internalColour(string, parseHex)
}

/**
 * Colour
 *
 * @param list
 * @param parseHex
 * @return
 */
fun colour(list: List<String>?, parseHex: Boolean = true): List<String>? {
    list?.forEach {string -> internalColour(string, parseHex)}
    return list
}

private fun replaceHex(string: String?): String? {
    var str = string
    if(str != null) {
        val matcher: Matcher = hexPattern.matcher(str)
        while (matcher.find()) {
            val group = matcher.group(1)
            str = str?.replace(group, ChatColor.of(group).toString())
        }
    } else { return "" }
    return str
}

private fun internalColour(str: String? = "", parseHex: Boolean): String? {
    return ChatColor.translateAlternateColorCodes('&', if(parseHex) replaceHex(str) else str)
}