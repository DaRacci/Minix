package me.racci.raccicore.command.lang

import co.aikar.commands.lib.expiringmap.ExpirationPolicy
import co.aikar.commands.lib.expiringmap.ExpiringMap
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object Patterns {

    val COMMA: Pattern = Pattern.compile(",")
    val PERCENTAGE: Pattern = Pattern.compile("%", Pattern.LITERAL)
    val NEWLINE: Pattern = Pattern.compile("\n")
    val DASH: Pattern = Pattern.compile("-")
    val UNDERSCORE: Pattern = Pattern.compile("_")
    val SPACE: Pattern = Pattern.compile(" ")
    val SEMICOLON: Pattern = Pattern.compile(";")
    val COLON: Pattern = Pattern.compile(":")
    val COLONEQUALS: Pattern = Pattern.compile("([:=])")
    val PIPE: Pattern = Pattern.compile("\\|")
    val NON_ALPHA_NUMERIC: Pattern = Pattern.compile("[^a-zA-Z0-9]")
    val INTEGER: Pattern = Pattern.compile("^[0-9]+$")
    val VALID_NAME_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9_$]{1,16}$")
    val NON_PRINTABLE_CHARACTERS: Pattern = Pattern.compile("[^\\x20-\\x7F]")
    val EQUALS: Pattern = Pattern.compile("=")
    val FORMATTER: Pattern = Pattern.compile("<c(?<color>\\d+)>(?<msg>.*?)</c\\1>", Pattern.CASE_INSENSITIVE)
    val I18N_STRING: Pattern = Pattern.compile("\\{@@(?<key>.+?)}", Pattern.CASE_INSENSITIVE)
    val REPLACEMENT_PATTERN: Pattern = Pattern.compile("%\\{.[^\\s]*}")

    val patternCache = ExpiringMap.builder()
        .maxSize(200)
        .expiration(1, TimeUnit.HOURS)
        .expirationPolicy(ExpirationPolicy.ACCESSED)
        .build<String, Pattern>()

    /**
     * Gets a pattern and compiles it.
     * If the pattern is stored already in [patternCache], it will simply fetch it from there.
     * If it is not, it will store it there for further use.
     *
     *
     * The [patternCache] does not contain the constant patterns defined in this class.
     *
     * @param pattern The raw pattern in a String.
     * @return The pattern which has been cached.
     */
    fun getPattern(pattern: String): Pattern {
        return patternCache.computeIfAbsent(pattern) {Pattern.compile(pattern)}
    }


}