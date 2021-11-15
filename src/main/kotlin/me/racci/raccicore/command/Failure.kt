package me.racci.raccicore.command

import me.racci.raccicore.extensions.asComponent
import me.racci.raccicore.extensions.miniMessage
import me.racci.raccicore.extensions.msg
import me.racci.raccicore.extensions.text
import me.racci.raccicore.utils.collections.ExpirationList
import me.racci.raccicore.utils.collections.ExpirationMap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

typealias ErrorHandler = Executor<*>.(Throwable) -> Unit

object Failure {

    val defaultErrorHandler: ErrorHandler = {
        sender.msg(
            miniMessage("<red>An internal error occurred whilst executing this command") {
                hoverEvent(it.toString().asComponent().color(NamedTextColor.RED))
            }
        ) // TODO LANG
        it.printStackTrace()
    }

    class CommandFailureException(
        val senderMessage: Component? = null,
        val argMissing: Boolean = false,
        inline val execute: suspend () -> Unit = {}
    ) : RuntimeException()

    fun Executor<*>.failure(
        senderMessage: Component? = null,
        execute: suspend () -> Unit = {}
    ): Nothing = throw CommandFailureException(senderMessage, execute = execute)

    fun Executor<*>.failure(
        senderMessage: List<Component> = listOf(),
        execute: suspend () -> Unit = {}
    ): Nothing = failure(text{senderMessage.takeIf{it.isNotEmpty()}?.map{append(it)}}, execute = execute)

// expiration collections

    inline fun <T> ExpirationList<T>.failIfContains(
        element: T,
        execute: (missingTime: Int) -> Unit
    ) {
        missingTime(element)?.let(execute)?.run {
            throw CommandFailureException()
        }
    }

    inline fun <K> ExpirationMap<K, *>.failIfContains(
        key: K,
        execute: (missingTime: Long) -> Unit
    ) {
        missingTime(key)?.let(execute)?.run {
            throw CommandFailureException()
        }
    }

}