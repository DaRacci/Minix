package me.racci.raccicore.utils.strings

import me.racci.raccicore.utils.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.regex.Pattern

private const val regex = "\\{(\\S+)}"
private val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)

fun textOf(string: String, builder: TextComponent.() -> Unit = {}) =
    LegacyComponentSerializer.legacySection().deserialize(string).decoration(TextDecoration.ITALIC, false).also(builder)

fun colouredTextOf1(string: String, builder: TextComponent.() -> Unit = {}) =
    (LegacyComponentSerializer.legacySection().deserialize(colour(string)).toBuilder().build())
        .apply(builder)


fun colouredTextOf(string: String, builder: TextComponent.() -> Unit = {}) =
    LegacyComponentSerializer.legacySection().deserialize(colour(string)).decoration(TextDecoration.ITALIC, false)


//{
//        LegacyComponentSerializer.legacySection().deserialize(colour(string))
//    }

    // : TextComponent = text {
//    append(LegacyComponentSerializer.legacySection().deserialize(colour(string)))
//}.apply(builder).also(builder)

fun colouredTextOf(list: List<String>, builder: TextComponent.() -> Unit = {}) = ArrayList<Component>().apply {
    list.forEach { add(LegacyComponentSerializer.legacySection().deserialize(colour(it)).decoration(TextDecoration.ITALIC, false)) }
}

fun replace(source: String, vararg replace: String) : String {
    var var1 = source
    val itr = replace.iterator()
    while(itr.hasNext()) {
        var i = 0
        val old = itr.next()
        val new = itr.next()
        if(source.indexOf(old, i).also { i = it } >= 0) {
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