package dev.racci.minix.api.plugin

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import dev.racci.minix.api.extensions.WithPlugin
import java.util.logging.Level

private val terminal by lazy { Terminal() }

@Suppress("MemberVisibilityCanBePrivate")
// TODO: Continue Indent for newlines
open class MinixLogger(
    override val plugin: MinixPlugin
) : WithPlugin<MinixPlugin> {

    private val prefix by lazy { TextColors.brightCyan("[${plugin.description.prefix}]") }
    private val trace by lazy { TextColors.white("[TRACE]") }
    private val info by lazy { TextColors.cyan("[INFO]") }
    private val warn by lazy { TextColors.yellow("[WARN]") }
    private val error by lazy { TextColors.red("[ERROR]") }
    private val debug by lazy { TextColors.magenta("[DEBUG]") }

    val debugEnabled: Boolean
        get() =
            when (plugin.logger.level) {
                Level.ALL, Level.FINEST, Level.FINER -> true
                else -> false
            }

    val errorEnabled: Boolean
        get() =
            plugin.logger.level != Level.OFF

    val infoEnabled: Boolean
        get() =
            plugin.logger.level != Level.OFF

    val traceEnabled: Boolean
        get() =
            when (plugin.logger.level) {
                Level.ALL, Level.FINEST -> true
                else -> false
            }

    val warnEnabled: Boolean
        get() =
            when (plugin.logger.level) {
                Level.OFF, Level.WARNING -> false
                else -> true
            }

    fun trace(
        t: Throwable? = null,
        msg: () -> Any?
    ) {
        if (traceEnabled) trace(msg.toStringSafe(), t)
    }

    fun debug(
        t: Throwable? = null,
        msg: () -> Any?
    ) {
        if (debugEnabled) debug(msg.toStringSafe(), t)
    }

    fun info(
        t: Throwable? = null,
        msg: () -> Any?
    ) {
        if (infoEnabled) info(msg.toStringSafe(), t)
    }

    fun warn(
        t: Throwable? = null,
        msg: () -> Any?
    ) {
        if (warnEnabled) warn(msg.toStringSafe(), t)
    }

    fun error(
        t: Throwable? = null,
        msg: () -> Any?
    ) {
        if (errorEnabled) error(msg.toStringSafe(), t)
    }

    inline fun <T : Throwable> throwing(throwable: T): T {
        if (errorEnabled) {
            error("throwing($throwable)", throwable)
        }
        return throwable
    }

    inline fun <T : Throwable> catching(throwable: T) {
        if (errorEnabled) {
            error("catching($throwable)", throwable)
        }
    }

    fun trace(
        msg: String? = null,
        throwable: Throwable? = null
    ) {
        if (!traceEnabled) return
        log(trace, msg, throwable, TextColors.gray)
    }

    fun debug(
        msg: String? = null,
        throwable: Throwable? = null
    ) {
        if (!debugEnabled) return
        log(debug, msg, throwable, TextColors.magenta)
    }

    fun info(
        msg: String? = null,
        throwable: Throwable? = null
    ) {
        if (!infoEnabled) return
        log(info, msg, throwable, TextColors.cyan)
    }

    fun warn(
        msg: String? = null,
        throwable: Throwable? = null
    ) {
        if (!warnEnabled) return
        log(warn, msg, throwable, TextColors.yellow)
    }

    fun error(
        msg: String? = null,
        throwable: Throwable? = null
    ) {
        if (!errorEnabled) return
        log(error, msg, throwable, TextColors.red)
    }

    private fun log(
        type: String,
        msg: String?,
        throwable: Throwable?,
        colour: TextColors
    ) {
        val builder = StringBuilder("$prefix $type ${TextColors.brightWhite("->")} ")
        if (msg != null) {
            val lines = msg.split("\n")
            for ((index, line) in lines.withIndex()) {
                if (index > 0) {
                    builder.append("\n\t\t")
                }
                builder.append(colour(line))
            }
        }
        throwable?.let { thrower ->

            builder.append("\n")
                .append(TextColors.yellow(thrower::class.qualifiedName ?: "null"))
                .append(TextColors.brightWhite(" -> "))
                .append(thrower.localizedMessage ?: thrower.cause?.localizedMessage ?: thrower.cause?.cause)

            var cause: Throwable? = thrower
            while (cause != null) {
                builder.append("\n\t\t" + TextColors.yellow(thrower::class.qualifiedName ?: "null") + TextColors.brightWhite(" -> "))
                builder.append(thrower.localizedMessage)

                if (debugEnabled) {
                    cause.stackTrace.forEach {
                        builder.append(TextColors.gray("\n\t\t\tat "))
                        builder.append(TextColors.white("${it.className}.${it.methodName}"))
                        builder.append(TextColors.yellow("(${it.fileName}:${it.lineNumber})"))
                    }
                }

                cause = cause.cause
            }
        }
        terminal.println(builder)
    }

    private inline fun (() -> Any?).toStringSafe(): String {
        return try {
            invoke().toString()
        } catch (e: Exception) {
            "Log message invocation failed: $e"
        }
    }
}
