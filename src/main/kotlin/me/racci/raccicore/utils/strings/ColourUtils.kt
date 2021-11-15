package me.racci.raccicore.utils.strings

import me.racci.raccicore.extensions.console
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.util.HSVLike
import net.md_5.bungee.api.ChatColor
import java.util.regex.Matcher
import java.util.regex.Pattern


private val hexPattern: Pattern = Pattern.compile("(#[A-Fa-f0-9]{6})")

/**
 * @param [string] Text to translate
 * @param [parseHex] Weather to translate hex or not, Defaults to true
 * @return Returns the translated [String] with [parseHex] for translating hex
 */
fun colour(string: String?, parseHex: Boolean = true) =
    internalColour(string, parseHex)

fun colour(string: Any?, parseHex: Boolean = true) =
    internalColour(string.toString(), parseHex)

/**
 * Colour
 *
 * @param list
 * @param parseHex
 * @return
 */
fun colour(list: List<String>, parseHex: Boolean = true): List<String> {
    list.forEach {string -> internalColour(string, parseHex)}
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

private fun internalColour(str: String? = "", parseHex: Boolean): String {
    return ChatColor.translateAlternateColorCodes('&', if(parseHex) replaceHex(str) else str)
}

fun gradient(h1: Float, s1: Float, v1: Float, h2: Float, s2: Float, v2: Float, string: String): Component {
    val c: TextComponent.Builder = Component.text("").toBuilder()
    val chars = string.toCharArray()
    var h = h1
    var s = s1
    var v = v1
    val hStep = (h2 - h1) / chars.size
    val sStep = (s2 - s1) / chars.size
    val vStep = (v2 - v1) / chars.size
    for (a in chars) {
        c.append(Component.text(a.toString()).color(TextColor.color(HSVLike.of(h / 360, s / 360, v / 360))))
        console.sendMessage(c)
        h += hStep
        s += sStep
        v += vStep
    }
    return c.decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true).build()
}