package me.racci.raccicore.command.lang

import co.aikar.commands.ACFUtil
import java.util.*
import java.util.regex.Matcher

abstract class MessageFormatter <C> {

    val colours: ArrayList<C?> = ArrayList() // TODO maybe please not null?????

    fun MessageFormatter(
        vararg colours: C
    ) = this.colours.addAll(colours)

    fun setColour(
        index: Int,
        colour: C
    ): C? {
        var i = index
        if(i > 0) i-- else i = 0
        return if(colours.size <= i) {
            val n = i - colours.size
            if(n > 0) colours.addAll(Collections.nCopies(n, null))
            colours.add(colour)
            null
        } else colours.set(index, colour)
    }

    fun getColour(
        index: Int
    ): C? {
        var i = index
        if(i > 0) i-- else i = 0
        var c = colours[index]
        if(c == null) c = getDefaultColour()
        return c
    }

    fun getDefaultColour(
    ) = getColour(1)

    abstract fun format(
        colour: C?,
        message: String
    ): String

    fun format(
        index: Int,
        message: String
    ) = format(colours[index], message)

    fun format(
        message: String
    ): String {
        val def = format(1, "")
        val matcher = Patterns.FORMATTER.matcher(message)
        val sb = StringBuffer(message.length)
        while(matcher.find()) {
            val c = ACFUtil.parseInt(matcher.group("colour"), 1) // TODO REPLACE
            val m = format(c, matcher.group("msg") + def)
            matcher.appendReplacement(sb, Matcher.quoteReplacement(m))
        }
        matcher.appendTail(sb)
        return def + sb.toString()
    }

}