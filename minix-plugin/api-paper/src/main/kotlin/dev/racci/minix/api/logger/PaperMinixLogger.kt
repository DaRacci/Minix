package dev.racci.minix.api.logger

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import jdk.internal.org.jline.terminal.TerminalBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KProperty

public class PaperMinixLogger<T>(
    override val plugin: T,
    loggingLevel: LoggingLevel = LoggingLevel.INFO
) : WithPlugin<T> by plugin, MinixLogger(loggingLevel) where T : MinixPlugin, T : WithPlugin<T> {

    override fun format(
        message: String,
        scope: String?,
        level: LoggingLevel,
        throwable: Throwable?,
        colour: TextColors?
    ): String = buildString {
        var appended = false
        val prefix = prefix(level, scope)
        val padding = prefix.length + 4

        append(applyColour(prefix, colour))

        append(' ')
        append(TextColors.brightWhite("->"))
        append(' ')

        formatMessage(message, padding).ifPresent { msg ->
            append(applyColour(msg, colour))
            appended = true
        }

        formatThrowable(throwable).ifPresent { thr ->
            append(thr)
            appended = true
        }

        if (!appended) {
            append(applyColour("Empty message.", colour)).toString()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    public override fun log(
        level: LoggingLevel,
        err: Throwable?,
        scope: String?,
        msg: UnsafeMessage,
        colour: TextColors?
    ) {
        plugin.launch(dispatcher) {
            super.log(level, err, scope, msg, colour)
        }

        if (server.isPrimaryThread) { // We don't want anything to do with the server thread.
            GlobalScope.launch(dispatcher) {
                logAction(message)
            }
        } else logAction(message)
    }

    override fun preProcess(
        level: LoggingLevel,
        err: Throwable?,
        scope: String?,
        msg: String?
    ) {
        if (err != null) Sentry.captureException(err)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun getCallerScope(): String? {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk { walker ->
            walker.skip(4)
                .filter { frame -> frame.declaringClass.superclass == Extension::class.java }
                .filter { frame -> frame.declaringClass.isAnnotationPresent(MappedExtension::class.java) }
                .map { frame -> frame.declaringClass.getAnnotation(MappedExtension::class.java).name }
                .findFirst()
        }.getOrNull()
    }

    private val generalLength = run {
        var length = 6 // [][][]
        length += 8 // HH:mm:ss
        length += 3 // Spaces
        length += 16 // Colours
        length += plugin.name.length

        length
    }

    private fun logAction(message: () -> FormattedMessage) {
        val formatted = message()

        addSentryBreadcrumb(formatted)
        appendLogFile(formatted)

        TERMINAL.writer().println(formatted.rendered)
        TERMINAL.writer().flush()
    }

    private fun applyColour(
        string: String,
        colour: TextColors?
    ): String = when (colour) {
        null -> string
        else -> colour(string)
    }

    private fun prefix(
        level: LoggingLevel,
        actualScope: String?
    ): String = buildString {
        append('[')
        append(getTime())

        append(' ')
        append(level.name)
        append("] [")
        append(plugin.name)

        if (!actualScope.isNullOrBlank()) {
            append(':')
            append(actualScope)
        }

        if (level.ordinal > 3) {
            append('/')
            append(Thread.currentThread().name)
        }

        append(']')
    }

    private fun formatMessage(
        message: String,
        padding: Int
    ): Optional<String> {
        if (message.isBlank()) return Optional.empty()

        return Optional.of(
            buildString {
                for ((index, line) in message.split('\n').withIndex()) {
                    if (index > 0) {
                        append('\n')
                        append("\t".repeat((padding / 4).coerceAtLeast(1)))
                    }

                    append(line.trimEnd())
                }
            }
        )
    }

    private fun formatThrowable(throwable: Throwable?): Optional<String> {
        if (throwable == null) return Optional.empty()

        val string = buildString {
            var lastCause: Throwable? = null
            var cause: Throwable? = throwable
            val stackTrace = isEnabled(LoggingLevel.DEBUG)

            repeat(3) { depth ->
                if (cause == null || cause === lastCause) return@repeat

                if (lastOrNull() != '\n') append('\n')

                if (depth > 0) {
                    append('\n')
                    append(TextColors.brightRed("Nested Caused by: "))
                } else append(TextColors.brightRed("Caused by: "))

                append(TextColors.brightCyan(cause!!::class.qualifiedName ?: "null"))
                append(TextColors.brightWhite(" -> "))
                append(TextColors.brightCyan(cause!!.message ?: "null"))

                if (stackTrace) {
                    for (element in cause!!.stackTrace) {
                        append(TextColors.gray("\n\tat "))
                        append(TextColors.white("${element.className}.${element.methodName}"))
                        append(TextColors.yellow("(${element.fileName}:${element.lineNumber})"))
                    }
                }

                lastCause = cause
                cause = cause!!.cause
            }
        }

        return Optional.of(string)
    }

    private fun appendLogFile(formatted: FormattedMessage) {
        val byteArray = buildString {
            append(logFilePrefix(formatted.scope, plugin.name, formatted.level))
            append(formatted.formatted.substring(getPrefixLength(formatted.scope, formatted.level)))
        }.toByteArray(Charsets.UTF_8)

        ROLLING_MANAGER.writeBytes(byteArray, 0, byteArray.size)
        ROLLING_MANAGER.writeBytes(NEWLINE, 0, NEWLINE.size)
    }

    private fun logFilePrefix(
        scope: String?,
        plugin: String?,
        level: LoggingLevel
    ): String = buildString {
        append('[')
        append(getTime())
        append("] [")

        append(Thread.currentThread().name)
        append('/')
        append(level.name)
        append("] [")

        append(plugin)
        if (scope != null) {
            append('/')
            append(scope)
        }

        append("] -> ")
    }

    private fun getTime(): String = buildString {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        append(time.hour.toString().padStart(2, '0'))
        append(':')
        append(time.minute.toString().padStart(2, '0'))
        append(':')
        append(time.second.toString().padStart(2, '0'))

//        if (level.ordinal > 4) {
//                append('.')
//                append(this.nanosecond)
//        }
    }

    private fun addSentryBreadcrumb(message: FormattedMessage) {
        val breadcrumb = Breadcrumb()
        breadcrumb.category = "LogTrace"
        breadcrumb.level = SentryLevel.DEBUG
        breadcrumb.message = message.raw
        breadcrumb.type = message.level.name

        val stackTrace = Thread.currentThread().stackTrace
        val calledFrom = stackTrace[4].className + " -> " + stackTrace[4].methodName
        breadcrumb.setData("calledFrom", calledFrom)

        Sentry.addBreadcrumb(breadcrumb)
    }

    private fun getPrefixLength(
        scope: String?,
        level: LoggingLevel
    ): Int {
        var length = generalLength
        length += level.name.length

        if (!scope.isNullOrBlank()) {
            length += scope.length + 1
        }

        if (level.ordinal > 3) {
            length += Thread.currentThread().name.length + 1
        }

        return length
    }

    init {
        if (plugin.getLogger().level != null) {
            this.setLevel(LoggingLevel.convert(plugin.getLogger().level))
        }
    }

    public companion object {
        private val NEWLINE: ByteArray = "\n".encodeToByteArray()
        private val TERMINAL: Terminal = TerminalConsoleAppender.getTerminal().toOption()
            .redeem({ TerminalBuilder.terminal() }, { it })
            .redeem({ TerminalBuilder.builder().build() }, { it })
            .getOrElse { error("Couldn't get the default Terminal Appender.") }
        private val ROLLING_MANAGER: RollingRandomAccessFileManager = AbstractManager::class.staticProperties
            .findKProperty<Map<String, AbstractManager>>("MAP")
            .map { runBlocking { it.accessGet() } }
            .mapNotNull { map -> map["logs/latest.log"] }
            .tapNone { error("Couldn't get 'latest.log' from the RollingRandomAccessFileManager.") }
            .filterIsInstance<RollingRandomAccessFileManager>()
            .getOrElse { error("The manager instance wasn't of type RollingRandomAccessFileManager.") }

        internal val EXISTING: MutableMap<MinixPlugin, PaperMinixLogger<*>> = mutableMapOf()

        public operator fun getValue(
            thisRef: WithPlugin<*>,
            property: KProperty<*>
        ): PaperMinixLogger<*> = EXISTING.getOrPut(thisRef.plugin) { PaperMinixLogger(thisRef.plugin) }

        public fun getLogger(
            plugin: MinixPlugin
        ): PaperMinixLogger<*> = EXISTING.getOrPut(plugin) { PaperMinixLogger(plugin) }
    }
}
