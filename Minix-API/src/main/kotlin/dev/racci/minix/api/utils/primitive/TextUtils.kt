package dev.racci.minix.api.utils.primitive

import dev.racci.minix.api.extensions.noItalic
import dev.racci.minix.api.utils.primitive.ColourUtils.colour
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

private const val regex = "\\{(\\S+)}"

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ScheduledForRemoval(inVersion = "3.0.0")
fun textOf(
    string: String,
    builder: TextComponent.() -> Unit = {},
) = LegacyComponentSerializer.legacySection().deserialize(string).noItalic().also(builder)

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ScheduledForRemoval(inVersion = "3.0.0")
fun textOf(
    list: List<String>,
    builder: List<TextComponent>.() -> Unit = {},
) = list.map { LegacyComponentSerializer.legacySection().deserialize(it).noItalic() }.also(builder)

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ScheduledForRemoval(inVersion = "3.0.0")
fun colouredTextOf(
    string: String,
    builder: TextComponent.() -> Unit = {},
) = LegacyComponentSerializer.legacySection().deserialize(colour(string)).noItalic().also(builder)

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ScheduledForRemoval(inVersion = "3.0.0")
fun colouredTextOf(
    list: List<String>,
    builder: List<TextComponent>.() -> Unit = {},
) = list.map { LegacyComponentSerializer.legacySection().deserialize(colour(it)).noItalic() }.also(builder)

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ScheduledForRemoval(inVersion = "3.0.0")
fun replace(
    source: String,
    vararg replace: String,
): String {
    var var1 = source
    val itr = replace.iterator()
    while (itr.hasNext()) {
        var i = 0
        val old = itr.next()
        val new = itr.next()
        if (source.indexOf(old, i).also { i = it } >= 0) {
            val sourceArray = source.toCharArray()
            val nsArray = new.toCharArray()
            val oLength = old.length
            val buf = StringBuilder(sourceArray.size)
            buf.append(sourceArray, 0, i).append(nsArray)
            i += oLength
            var j = i
            // Replace all remaining instances of oldString with newString.
            while (source.indexOf(old, i).also { i = it } > 0) {
                buf.append(sourceArray, j, i - j).append(nsArray)
                i += oLength
                j = i
            }
            buf.append(sourceArray, j, sourceArray.size - j)
            var1 = buf.toString()
            buf.setLength(0)
        }
    }
    return var1
}
