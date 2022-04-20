package dev.racci.minix.api.utils.primitive

import net.md_5.bungee.api.ChatColor
import org.jetbrains.annotations.ApiStatus
import java.util.regex.Matcher
import java.util.regex.Pattern

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
object ColourUtils {

    private val hexPattern = Pattern.compile("(#[A-Fa-f0-9]{6})")

    @Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
    @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
    fun colour(
        string: String?,
        parseHex: Boolean = true,
    ): String = internalColour(string, parseHex)

    @Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
    @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
    fun colour(
        string: Any?,
        parseHex: Boolean = true,
    ): String = internalColour(string.toString(), parseHex)

    @Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
    @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
    fun colour(
        list: List<String>,
        parseHex: Boolean = true,
    ) = list.onEach { internalColour(it, parseHex) }

    private fun replaceHex(string: String?): String? {
        var str = string
        if (str != null) {
            val matcher: Matcher = hexPattern.matcher(str)
            while (matcher.find()) {
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
        parseHex: Boolean,
    ) = ChatColor.translateAlternateColorCodes('&', if (parseHex) replaceHex(str) else str)
}
