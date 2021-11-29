@file:Suppress("UNUSED")
package me.racci.raccicore.api.utils.primitive

import net.md_5.bungee.api.ChatColor
import java.util.regex.Matcher
import java.util.regex.Pattern

object ColourUtils {

    private val hexPattern = Pattern.compile("(#[A-Fa-f0-9]{6})")

    fun colour(
        string: String?,
        parseHex: Boolean = true
    ): String = internalColour(string, parseHex)

    fun colour(
        string: Any?,
        parseHex: Boolean = true
    ): String = internalColour(string.toString(), parseHex)

    fun colour(
        list: List<String>,
        parseHex: Boolean = true
    ) = list.onEach{internalColour(it, parseHex)}

    private fun replaceHex(string: String?): String? {
        var str = string
        if(str != null) {
            val matcher: Matcher = hexPattern.matcher(str)
            while(matcher.find()) {
                val group = matcher.group(1)
                str = str?.replace(group, ChatColor.of(group).toString())
            }
        } else {
            return ""
        }
        return str
    }

    private fun internalColour(
        str: String? = "",
        parseHex: Boolean
    ) = ChatColor.translateAlternateColorCodes('&', if(parseHex) replaceHex(str) else str)

}