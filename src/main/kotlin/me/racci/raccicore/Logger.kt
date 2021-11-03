package me.racci.raccicore

import me.racci.raccicore.utils.console
import me.racci.raccicore.utils.strings.colour
import org.jetbrains.annotations.ApiStatus

@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().<level>(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun log(level: Level, message: String) {
    when(level) {

        Level.ERROR     -> console.sendMessage(colour("&4[&c&lERROR&4] &f$message", true))
        Level.WARNING   -> console.sendMessage(colour("&e[&6&lWARNING&e] &f$message", true))
        Level.INFO      -> console.sendMessage(colour("&8[&e&lINFO&8] &f$message", true))
        Level.DEBUG     -> console.sendMessage(colour("&7[&f&lDEBUG&7] &f$message", true))
        Level.SUCCESS   -> console.sendMessage(colour("&2[&a&lSUCCESS&2] &f$message", true))
        Level.OUTLINE   -> console.sendMessage(colour("&7$message", true))
    }
}

class Log(prefix: String = "", var debugMode: Boolean = false) {
    private val p = if(prefix == "") prefix else prefix.plus(" ")

    fun info(message: String = "") =
        println(colour("$p&e[&lINFO&e] $message"))
    fun success(message: String) =
        println(colour("$p&a[&lSUCCESS&a] $message"))
    fun warning(message: String) =
        println(colour("$p&c[&lWARNING&c] $message"))
    fun error(message: String) {
        println(colour("&4&l-------------------------------"))
        println("")
        println(colour("$p&4[&lERROR&4] $message"))
        println("")
        println(colour("&4&l-------------------------------"))
    }
    fun debug(message: String) {
        if(debugMode) println(colour("$p&5[&lDEBUG&5] $message"))
    }
}

@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().error(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun error(message: String) {
    log(Level.ERROR, message)
}
@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().warning(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun warning (message: String) {
    log(Level.WARNING, message)
}
@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().info(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun info(message: String) {
    log(Level.INFO, message)
}
@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().debug(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun debug(message: String) {
    log(Level.DEBUG, message)
}
@Deprecated("Deprecated in favour for a more modular system.", ReplaceWith("Log().success(message)"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun success(message: String) {
    log(Level.SUCCESS, message)
}
@Deprecated("Deprecated as its pretty useless")
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
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