package me.racci.raccicore

import me.racci.raccicore.utils.strings.colour
import org.bukkit.Bukkit

/**
 * Log
 *
 * @param level
 * @param message
 */
fun log(level: Level, message: String) {
    when(level) {
        Level.ERROR -> Bukkit.getConsoleSender().sendMessage(colour("&4[&c&lERROR&4] &f$message", true))
        Level.WARNING -> Bukkit.getConsoleSender().sendMessage(colour("&e[&6&lWARNING&e] &f$message", true))
        Level.INFO -> Bukkit.getConsoleSender().sendMessage(colour("&8[&e&lINFO&8] &f$message", true))
        Level.DEBUG -> Bukkit.getConsoleSender().sendMessage(colour("&7[&f&lDEBUG&7] &f$message", true))
        Level.SUCCESS -> Bukkit.getConsoleSender().sendMessage(colour("&2[&a&lSUCCESS&2] &f$message", true))
        Level.OUTLINE -> Bukkit.getConsoleSender().sendMessage(colour("&7$message", true))
    }
}

fun error(message: String) {
    log(Level.ERROR, message)
}
fun warning (message: String) {
    log(Level.WARNING, message)
}
fun info(message: String) {
    log(Level.INFO, message)
}
fun debug(message: String) {
    log(Level.DEBUG, message)
}
fun success(message: String) {
    log(Level.SUCCESS, message)
}
fun outline(message: String) {
    log(Level.OUTLINE, message)
}

/**
 * Level
 *
 * @constructor Create empty Level
 */
enum class Level {
    /**
     * E r r o r
     *
     * @constructor Create empty E r r o r
     */
    ERROR,

    /**
     * W a r n i n g
     *
     * @constructor Create empty W a r n i n g
     */
    WARNING,

    /**
     * I n f o
     *
     * @constructor Create empty I n f o
     */
    INFO,

    /**
     * D e b u g
     *
     * @constructor Create empty D e b u g
     */
    DEBUG,

    /**
     * S u c c e s s
     *
     * @constructor Create empty S u c c e s s
     */
    SUCCESS,

    /**
     * O u t l i n e
     *
     * @constructor Create empty O u t l i n e
     */
    OUTLINE }