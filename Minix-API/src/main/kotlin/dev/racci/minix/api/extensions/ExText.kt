@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.primitive.ColourUtils.colour
import dev.racci.minix.api.utils.primitive.LegacyUtils
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Sends this array of [ComponentLike]s to the [Player]
 *
 * @param messages The messages to send.
 */
fun Player.msg(messages: Array<ComponentLike>): Unit = messages.forEach(::msg)

/**
 * Sends this collection of [ComponentLike]s to the [Player]
 *
 * @param messages The messages to send.
 */
fun Player.msg(messages: Collection<ComponentLike>): Unit = messages.forEach(::msg)

/**
 * Sends this array of [ComponentLike]s to the [CommandSender]
 *
 * @param messages The messages to send.
 */
fun CommandSender.msg(messages: Array<ComponentLike>): Unit = messages.forEach(::msg)

/**
 * Sends this collection of [ComponentLike]s to the [CommandSender]
 *
 * @param messages The messages to send.
 */
fun CommandSender.msg(messages: Collection<ComponentLike>): Unit = messages.forEach(::msg)

/**
 * Sends this [ComponentLike] to the [Player]
 *
 * @param message The message to send.
 */
fun Player.msg(message: String) = sendMessage(message)

/**
 * Sends this [ComponentLike] to the [Player]
 *
 * @param message The message to send.
 */
fun Player.msg(message: ComponentLike) = sendMessage(message)

/**
 * Sends this [ComponentLike] to the [CommandSender]
 *
 * @param message The message to send.
 */
fun CommandSender.msg(message: String) = sendMessage(message)

/**
 * Sends this [ComponentLike] to the [CommandSender]
 *
 * @param message The message to send.
 */
fun CommandSender.msg(message: ComponentLike) = sendMessage(message)

fun String.parse() = miniMessage().parse(this)
fun Array<String>.parse() = map { miniMessage().parse(it) }.toTypedArray()
fun Collection<String>.parse() = map { miniMessage().parse(it) }

fun String.parseLegacy() = LegacyUtils.parseLegacy(this)
fun Array<String>.parseLegacy() = LegacyUtils.parseLegacy(this.toList()).toTypedArray()
fun Collection<String>.parseLegacy() = LegacyUtils.parseLegacy(this.toList())

fun String.coloured() = colour(this, true)

/**
 * Sends this message as a chat message to the given [CommandSender].
 *
 * @param recipient The recipient of the message.
 */
infix fun Component.message(recipient: CommandSender) = recipient.msg(this)

/**
 * Sends this message as a chat message to the audience of players.
 *
 * @param audience The audience of players.
 */
infix fun Component.message(audience: Audience) = audience.sendMessage(this)

/**
 * Sends this message as a chat message to the given [CommandSender].
 *
 * @param recipient The recipient of the message.
 */
infix fun String.message(recipient: CommandSender) = recipient.msg(this)
