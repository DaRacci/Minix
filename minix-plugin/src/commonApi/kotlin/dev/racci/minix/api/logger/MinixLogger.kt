package dev.racci.minix.api.logger

import arrow.core.getOrElse
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import dev.racci.minix.api.exceptions.NoTraceException
import dev.racci.minix.api.extensions.collections.findKFunction
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.utils.kotlin.toSafeString
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import org.apiguardian.api.API
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

internal typealias UnsafeMessage = (() -> Any?)?

/**
 * The logger used by Minix.
 * This is a wrapper around the logger used by the platform.
 * This is also used to provide a consistent logging experience across plugins.
 * This class is thread-safe and provides a thread to process log messages so to not block the main thread.
 */
@API(status = API.Status.MAINTAINED, since = "5.0.0")
public abstract class MinixLogger(level: LoggingLevel) {
    protected open val dispatcher: CoroutineDispatcher = thread.get()

    @get:ScheduledForRemoval(inVersion = "5.0.0")
    @get:Deprecated("Use logLevel instead.", ReplaceWith("logLevel"))
    public val level: LoggingLevel get() = logLevel

    /** The current logging level. */
    public val logLevel: LoggingLevel get() = atomicLevel.value

    /**
     * @param level The [LoggingLevel] to test.
     * @return `true` if the given [LoggingLevel] can display.
     */
    public fun isEnabled(level: LoggingLevel): Boolean = level <= this.logLevel

    /**
     * Sets the [LoggingLevel] of this logger.
     * If [atomicLock] is `true` this will not change the level.
     *
     * @param level the [LoggingLevel] to log at.
     * @return The previous [LoggingLevel] and makes a trace log, or if the same just returns.
     */
    public fun setLevel(level: LoggingLevel): LoggingLevel {
        if (this.atomicLock.value) {
            this.warn { "Attempted to change the level of a locked logger." }
            return this.logLevel
        }

        if (level === this.logLevel) return level

        val previous = this.atomicLevel.getAndSet(level)
        this.trace { "$previous -> ${this.atomicLevel.value}" }

        return previous
    }

    /** Do anything that needs to be done before the message is possibly logged (Level not checked yet). */
    protected open fun preProcess(
        level: LoggingLevel,
        err: Throwable?,
        scope: String?,
        msg: String?
    ): Unit = Unit

    /**
     * Allows the processing and modification of the message before logged.
     *
     * @param message the raw message.
     * @param level the [LoggingLevel] of the message.
     * @param throwable the [Throwable] to format.
     * @param colour the [TextColors] of the message.
     * @return the formatted message.
     */
    protected open fun format(
        message: String,
        scope: String?,
        level: LoggingLevel,
        throwable: Throwable?,
        colour: TextColors?
    ): String = message

    /**
     * Process the log.
     *
     * @param level the [LoggingLevel] of the message.
     * @param err the [Throwable] to log.
     * @param scope the scope of the message.
     * @param msg the message to log.
     * @param colour the [TextColors] of the message.
     */
    protected open fun log(
        level: LoggingLevel,
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null,
        colour: TextColors? = null
    ) {
        thread.get().dispatch(dispatcher) {
            this.preProcess(level, err, scope, msg?.toSafeString())
            if (!isEnabled(level)) return@dispatch

            val formattedMessage = FormattedMessage(msg, scope, level, err, colour)
            this.printLog(formattedMessage)
        }
    }

    /** Print the log. */
    protected open fun printLog(formattedMessage: FormattedMessage) {
        rawPrintln.call(TERMINAL, formattedMessage.rendered)
    }

    /**
     * Logs a message at the [LoggingLevel.TRACE] level.
     *
     * @param err the [Throwable] that caused this log or null.
     * @param msg the message to log.
     * @param scope the scope of the message.
     */
    public open fun trace(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): Unit = log(
        LoggingLevel.TRACE,
        err,
        scope ?: getCallerScope(),
        msg,
        TextColors.brightMagenta
    )

    /**
     * Logs a message at the [LoggingLevel.DEBUG] level.
     *
     * @param err the [Throwable] that caused this log or null.
     * @param msg the message to log.
     * @param scope the scope of the message.
     */
    public open fun debug(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): Unit = log(
        LoggingLevel.DEBUG,
        err,
        scope ?: getCallerScope(),
        msg,
        TextColors.brightBlue
    )

    /**
     * Logs a message at the [LoggingLevel.INFO] level.
     *
     * @param err the [Throwable] that caused this log or null.
     * @param msg the message to log.
     * @param scope the scope of the message.
     */
    public open fun info(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): Unit = log(
        LoggingLevel.INFO,
        err,
        scope ?: getCallerScope(),
        msg,
        TextColors.cyan
    )

    /**
     * Logs a message at the [LoggingLevel.WARN] level.
     *
     * @param err the [Throwable] that caused this log or null.
     * @param msg the message to log.
     * @param scope the scope of the message.
     */
    public open fun warn(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): Unit = log(
        LoggingLevel.WARN,
        err,
        scope ?: getCallerScope(),
        msg,
        TextColors.yellow
    )

    /**
     * Logs a message at the [LoggingLevel.ERROR] level.
     *
     * @param err the [Throwable] that caused this log or null.
     * @param msg the message to log.
     * @param scope the scope of the message.
     */
    public open fun error(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): Unit = log(
        LoggingLevel.ERROR,
        err,
        scope ?: getCallerScope(),
        msg,
        TextColors.red
    )

    /**
     * Logs an error, which is unrecoverable.
     *
     * @param err The [Throwable] to log.
     * @param scope The scope of the message.
     * @param msg The message to log.
     * @return A [NoTraceException] which has no stacktrace.
     */
    public open fun fatal(
        err: Throwable? = null,
        scope: String? = null,
        msg: UnsafeMessage = null
    ): NoTraceException {
        log(
            LoggingLevel.FATAL,
            err,
            scope ?: getCallerScope(),
            msg,
            TextColors.brightRed
        )
        return NoTraceException(cause = err)
    }

    @get:API(status = API.Status.INTERNAL)
    protected val atomicLock: AtomicBoolean = atomic(false)

    @get:API(status = API.Status.INTERNAL)
    protected val atomicLevel: AtomicRef<LoggingLevel> = atomic(level)

    /** Caller sensitive. */
    @API(status = API.Status.INTERNAL)
    protected abstract fun getCallerScope(): String?

    @API(status = API.Status.INTERNAL)
    internal fun lockLevel() {
        this.atomicLock.compareAndSet(expect = false, update = true)
    }

    @API(status = API.Status.INTERNAL)
    internal fun unlockLevel() {
        this.atomicLock.compareAndSet(expect = true, update = false)
    }

    public companion object {
        protected val TERMINAL: Terminal = Terminal(tabWidth = 4, interactive = false, hyperlinks = true)
        protected val rawPrintln: KFunction<Unit> = Terminal::class.declaredMemberFunctions
            .findKFunction<Unit>("rawPrintln")
            .tap { it.isAccessible = true }
            .getOrElse { error("Could not find rawPrintln function.") }

        // TODO -> Close on shutdown
        @OptIn(DelicateCoroutinesApi::class)
        protected val thread: Closeable<ExecutorCoroutineDispatcher> = object : Closeable<ExecutorCoroutineDispatcher>() {
            override fun create(): ExecutorCoroutineDispatcher = newSingleThreadContext("MinixLogger Thread")
            override fun onClose() { this.value.value?.close() }
        }
    }

    @API(status = API.Status.STABLE, since = "3.2.0")
    public inner class FormattedMessage(
        input: String?,
        public val scope: String?,
        public val level: LoggingLevel,
        public val throwable: Throwable?,
        textColour: TextColors?
    ) {

        public constructor(
            input: (() -> Any?)?,
            scope: String?,
            level: LoggingLevel,
            throwable: Throwable?,
            textColour: TextColors?
        ) : this(input.toSafeString(), scope, level, throwable, textColour)

        public val raw: String

        public val formatted: String

        public val rendered: String

        init {
            this.raw = input.orEmpty()
            this.formatted = format(raw, scope, level, throwable, textColour)
            this.rendered = TERMINAL.render(formatted)
        }
    }
}
