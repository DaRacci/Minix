package me.racci.raccicore.utils.strings

import java.util.regex.Pattern

private const val regex = "\\{(\\S+)}"
private val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)

/**
 * Replace
 *
 * @param var1
 * @param os
 * @param ns
 * @return
 */
fun replace(var1: String, os: String, ns: String): String {
    var source = var1
    var i = 0
    if (source.indexOf(os, i).also { i = it } >= 0) {
        val sourceArray = source.toCharArray()
        val nsArray = ns.toCharArray()
        val oLength = os.length
        val buf = StringBuilder(sourceArray.size)
        buf.append(sourceArray, 0, i).append(nsArray)
        i += oLength
        var j = i
        // Replace all remaining instances of oldString with newString.
        while (source.indexOf(os, i).also { i = it } > 0) {
            buf.append(sourceArray, j, i - j).append(nsArray)
            i += oLength
            j = i
        }
        buf.append(sourceArray, j, sourceArray.size - j)
        source = buf.toString()
        buf.setLength(0)
    }
    return source
}

/**
 * Replace
 *
 * @param source
 * @param os1
 * @param ns1
 * @param os2
 * @param ns2
 * @param os3
 * @param ns3
 * @param os4
 * @param ns4
 * @param os5
 * @param ns5
 * @param os6
 * @param ns6
 * @return
 */
fun replace(
    source: String,
    os1: String,
    ns1: String,
    os2: String = "",
    ns2: String = "",
    os3: String = "",
    ns3: String = "",
    os4: String = "",
    ns4: String = "",
    os5: String = "",
    ns5: String = "",
    os6: String = "",
    ns6: String = "",
): String {
    return replace(replace(replace(
        replace(replace(replace(source,
            os1, ns1),
            os2, ns2),
            os3, ns3),
            os4, ns4),
            os5, ns5),
            os6, ns6)
}