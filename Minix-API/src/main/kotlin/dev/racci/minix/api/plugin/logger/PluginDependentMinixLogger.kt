package dev.racci.minix.api.plugin.logger

import com.github.ajalt.mordant.rendering.TextColors
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.unsafeCast
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.core.Logger
import org.apache.logging.slf4j.Log4jLogger
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class PluginDependentMinixLogger<T : MinixPlugin>(
    override val plugin: T,
    loggingLevel: LoggingLevel = LoggingLevel.INFO
) : MinixLogger(loggingLevel), WithPlugin<T> {
    private var logger: Logger? = null

    init {
        if (plugin.logger.level != null) {
            this.setLevel(LoggingLevel.fromJava(plugin.logger.level))
        }
    }

    override fun format(
        message: String,
        scope: String?,
        level: LoggingLevel,
        throwable: Throwable?,
        colour: TextColors?
    ): String {
        val actualScope = scope ?: extensionScope()
        val builder = StringBuilder(TextColors.brightWhite("->")).append(' ')
        var appended = false

        if (level.ordinal > 3) builder.append(colour?.invoke("[${level.name.capitalize()}] ") ?: "[${level.name.capitalize()}] ")
        if (!actualScope.isNullOrBlank()) append(builder, "[$actualScope] ", colour)

        if (message.isNotEmpty()) {
            val lines = message.split('\n')
            lines.forEachIndexed { index, contents ->
                if (index > 0) builder.append("\n\t")
                val trimmed = contents.trim()
                appended = append(builder, trimmed, colour)
            }
        }

        if (throwable != null) {
            var cause: Throwable? = throwable
            var attempts: Int = -1
            val stackTrace = isEnabled(LoggingLevel.DEBUG)

            while (cause != null && attempts++ < 3) {
                if (!builder.last().isWhitespace() && builder.last() != '\n') builder.append(' ')
                if (attempts != 0) {
                    builder.append("\n")
                    builder.append(TextColors.brightRed("Nested Caused by: "))
                } else {
                    builder.append(TextColors.brightRed("Caused by: "))
                }

                builder.append(TextColors.brightCyan(cause::class.qualifiedName ?: "null"))
                builder.append(TextColors.brightWhite(" -> "))
                builder.append(TextColors.brightCyan(cause.message ?: "null"))

                if (stackTrace) {
                    cause.stackTrace.forEach {
                        builder.append(TextColors.gray("\n\tat "))
                        builder.append(TextColors.white("${it.className}.${it.methodName}"))
                        builder.append(TextColors.yellow("(${it.fileName}:${it.lineNumber})"))
                    }
                }

                cause = cause.cause
            }

            appended = true
        }

        if (!appended) {
            builder.append("Empty message.").toString()
        }

        return builder.toString()
    }

    // TODO: Bypass formatting and use the stream directly.
    override fun log(message: FormattedMessage) {
        val level = message.level.takeUnless { it.ordinal > 3 } ?: LoggingLevel.INFO // Only INFO, WARN, and ERROR are supported.
        val logAtLevel = level.toLog4J()

        addSentryBreadcrumb(message)
        getLogger().log(logAtLevel, null as Marker?, message.rendered)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun extensionScope(): String? {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk { stream ->
            stream.skip(11).map(StackWalker.StackFrame::getDeclaringClass).filter { it.superclass == Extension::class.java }.findFirst().map { clazz ->
                val annotation = clazz.getAnnotation(MappedExtension::class.java)
                annotation.name
            }
        }.getOrNull()
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

    private fun append(
        builder: StringBuilder,
        text: String,
        colour: TextColors?
    ): Boolean {
        if (text.isEmpty()) return false

        when (colour) {
            null -> builder.append(text)
            else -> builder.append(colour(text))
        }

        return true
    }

    private fun getLogger(): Logger {
        if (logger != null) return logger!!

        val loggerProp = Log4jLogger::class.memberProperties.first { it.name == "logger" }
        loggerProp.isAccessible = true
        logger = loggerProp.get(plugin.slF4JLogger.unsafeCast()) as Logger
        loggerProp.isAccessible = false

        return logger!!
    }
}
