package dev.racci.minix.api.plugin.logger

import com.github.ajalt.mordant.rendering.OverflowWrap
import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.terminal.Terminal
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.exceptions.NoTraceException
import dev.racci.minix.api.utils.kotlin.fromOrdinal
import dev.racci.minix.api.utils.kotlin.toSafeString
import dev.racci.minix.api.utils.unsafeCast
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import org.apache.logging.log4j.spi.StandardLevel
import org.apiguardian.api.API
import java.util.logging.Level
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@OptIn(MinixInternal::class)
@API(status = API.Status.EXPERIMENTAL, since = "3.2.0")
abstract class MinixLogger {

    @MinixInternal
    private val atomicLock: AtomicBoolean = atomic(false)

    @MinixInternal
    private val atomicLevel: AtomicRef<LoggingLevel>

    val level get() = atomicLevel.value

    @MinixInternal
    fun lockLevel() {
        this.atomicLock.compareAndSet(expect = false, update = true)
    }

    @MinixInternal
    fun unlockLevel() {
        this.atomicLock.compareAndSet(expect = true, update = false)
    }

    constructor(level: LoggingLevel) {
        this.atomicLevel = atomic(level)
    }

    constructor(level: Level) {
        this.atomicLevel = atomic(LoggingLevel.fromJava(level))
    }

    constructor(level: org.apache.logging.log4j.Level) {
        this.atomicLevel = atomic(LoggingLevel.fromLog4J(level))
    }

    constructor(level: org.slf4j.event.Level) {
        this.atomicLevel = atomic(LoggingLevel.fromSlF4(level))
    }

    /**
     * @param level The [LoggingLevel] to test.
     * @return `true` if the given [LoggingLevel] can display.
     */
    fun isEnabled(level: LoggingLevel): Boolean = level.ordinal <= this.level.ordinal

    /**
     * Sets the [LoggingLevel] of this logger.
     * If [atomicLock] is `true` this will not change the level.
     *
     * @param level the [LoggingLevel] to log at.
     * @return The previous [LoggingLevel] and makes a trace log, or if the same just returns.
     */
    fun setLevel(level: LoggingLevel): LoggingLevel {
        if (this.atomicLock.value) return this.level
        if (level == this.level) return level

        val previous = this.atomicLevel.getAndSet(level)
        this.debug { "${this.atomicLevel} -> $level" }

        return previous
    }

    /**
     * Allows the processing and modification of the message before logged.
     *
     * @param message the raw message.
     * @param level the [LoggingLevel] of the message.
     * @param throwable the [Throwable] to format.
     * @param colour the [TextColors] of the message.
     * @return the filtered message.
     */
    protected open fun format(
        message: String,
        scope: String?,
        level: LoggingLevel,
        throwable: Throwable?,
        colour: TextColors?
    ): String = message

    /**
     * Finish processing and print the log.
     *
     * @param message The [FormattedMessage] to log.
     */
    open fun log(
        message: FormattedMessage
    ) = rawPrintln.call(terminal, message.rendered)

    open fun trace(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ) = ifLoggable(LoggingLevel.TRACE) { log(FormattedMessage(msg, scope, LoggingLevel.TRACE, t, TextColors.brightWhite)) }

    open fun debug(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ) = ifLoggable(LoggingLevel.DEBUG) { log(FormattedMessage(msg, scope, LoggingLevel.DEBUG, t, TextColors.brightBlue)) }

    open fun info(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ) = ifLoggable(LoggingLevel.INFO) { log(FormattedMessage(msg, scope, LoggingLevel.INFO, t, TextColors.cyan)) }

    open fun warn(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ) = ifLoggable(LoggingLevel.WARN) { log(FormattedMessage(msg, scope, LoggingLevel.WARN, t, TextColors.yellow)) }

    open fun error(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ) = ifLoggable(LoggingLevel.ERROR) { log(FormattedMessage(msg, scope, LoggingLevel.ERROR, t, TextColors.red)) }

    /**
     * Logs an error, which is unrecoverable.
     *
     * @param t The [Throwable] to log.
     * @param scope The scope of the message.
     * @param msg The message to log.
     * @return A [RuntimeException] without a stacktrace.
     */
    open fun fatal(
        t: Throwable? = null,
        scope: String? = null,
        msg: () -> Any?
    ): RuntimeException {
        log(FormattedMessage(msg, scope, LoggingLevel.FATAL, t, TextColors.brightRed))
        return NoTraceException(cause = t)
    }

    open fun trace(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ) = ifLoggable(LoggingLevel.TRACE) { log(FormattedMessage(msg, scope, LoggingLevel.TRACE, t, TextColors.brightWhite)) }

    open fun debug(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ) = ifLoggable(LoggingLevel.DEBUG) { log(FormattedMessage(msg, scope, LoggingLevel.DEBUG, t, TextColors.brightBlue)) }

    open fun info(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ) = ifLoggable(LoggingLevel.INFO) { log(FormattedMessage(msg, scope, LoggingLevel.INFO, t, TextColors.cyan)) }

    open fun warn(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ) = ifLoggable(LoggingLevel.WARN) { log(FormattedMessage(msg, scope, LoggingLevel.WARN, t, TextColors.yellow)) }

    open fun error(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ) = ifLoggable(LoggingLevel.ERROR) { log(FormattedMessage(msg, scope, LoggingLevel.ERROR, t, TextColors.red)) }

    /**
     * Logs an error, which is unrecoverable.
     *
     * @param t The [Throwable] to log.
     * @param msg The message to log.
     * @return A [RuntimeException] without a stacktrace.
     */
    open fun fatal(
        t: Throwable? = null,
        scope: String? = null,
        msg: String? = null
    ): RuntimeException {
        log(FormattedMessage(msg, scope, LoggingLevel.FATAL, t, TextColors.brightRed))
        return NoTraceException(cause = t)
    }

    /**
     * Runs the action if the [LoggingLevel] is enabled.
     *
     * @param level The [LoggingLevel] to test.
     * @param action The action to run.
     */
    protected fun ifLoggable(
        level: LoggingLevel,
        action: () -> Unit
    ) {
        if (!isEnabled(level)) return

        action()
    }

    protected companion object {
        val terminal = Terminal(tabWidth = 4, interactive = true, hyperlinks = false)
        val rawPrintln: KFunction<Unit>

        init {
            val func = Terminal::class.declaredMemberFunctions.first { it.name == "rawPrintln" }
            func.isAccessible = true
            rawPrintln = func.unsafeCast()
        }
    }

    @API(status = API.Status.STABLE, since = "3.2.0")
    inner class FormattedMessage(
        input: String?,
        scope: String?,
        val level: LoggingLevel,
        val throwable: Throwable?,
        textColour: TextColors?,
        whitespace: Whitespace = Whitespace.PRE,
        alignment: TextAlign = TextAlign.NONE,
        overflowWrap: OverflowWrap = OverflowWrap.NORMAL,
        width: Int = terminal.info.width
    ) {

        constructor(
            input: () -> Any?,
            scope: String?,
            level: LoggingLevel,
            throwable: Throwable?,
            textColour: TextColors?,
            whitespace: Whitespace = Whitespace.PRE,
            alignment: TextAlign = TextAlign.NONE,
            overflowWrap: OverflowWrap = OverflowWrap.NORMAL,
            width: Int = terminal.info.width
        ) : this(input.toSafeString(), scope, level, throwable, textColour, whitespace, alignment, overflowWrap, width)

        val raw: String

        val formatted: String

        val rendered: String

        init {
            this.raw = input.orEmpty()
            this.formatted = format(raw, scope, level, throwable, textColour)
            this.rendered = terminal.render(formatted, whitespace, alignment, overflowWrap, width)
        }
    }

    /**
     * A Utility for using levels between common loggers.
     * [LoggingLevel]'s are sorted by high level to low-level logs.
     * This allows for easy filtering using [Int] comparisons.
     */
    @API(status = API.Status.STABLE, since = "3.2.0")
    enum class LoggingLevel {
        FATAL, ERROR, WARN, INFO, DEBUG, TRACE;

        inline fun <reified T : Any> convert(): T {
            return when {
                Level.ALL is T -> this.toJava() as T
                org.apache.logging.log4j.Level.ALL is T -> this.toLog4J() as T
                org.slf4j.event.Level.TRACE is T -> this.toSlF4() as T
                else -> throw IllegalArgumentException("Unsupported type: ${T::class.qualifiedName}")
            }
        }

        fun toJava(): Level {
            return when (this) {
                FATAL -> Level.OFF
                ERROR -> Level.SEVERE
                WARN -> Level.WARNING
                INFO -> Level.INFO
                DEBUG -> Level.CONFIG
                TRACE -> Level.FINEST
            }
        }

        fun toSlF4(): org.slf4j.event.Level {
            return Enum.fromOrdinal(this.ordinal - SLF4)
        }

        fun toLog4J(): org.apache.logging.log4j.Level {
            val ordinal = ((this.ordinal + 1) * 100)
            val standardLevel = StandardLevel.getStandardLevel(ordinal).name
            return org.apache.logging.log4j.Level.getLevel(standardLevel)
        }

        companion object {
            private const val SLF4: Int = 1

            // TODO: Is there a math equation for this?
            fun fromJava(log: Level): LoggingLevel {
                return when (log) {
                    Level.ALL, Level.FINEST -> TRACE
                    Level.FINER, Level.FINE, Level.CONFIG -> DEBUG
                    Level.INFO -> INFO
                    Level.WARNING -> WARN
                    Level.SEVERE -> ERROR
                    Level.OFF -> FATAL
                    else -> throw LevelConversionException(log)
                }
            }

            fun fromSlF4(log: org.slf4j.event.Level): LoggingLevel {
                return Enum.fromOrdinal(log.ordinal + SLF4)
            }

            fun fromLog4J(log: org.apache.logging.log4j.Level): LoggingLevel {
                val ordinal = ((log.intLevel() / 100 % 10) - 1).coerceAtLeast(0)
                return Enum.fromOrdinal(ordinal)
            }
        }
    }
}
