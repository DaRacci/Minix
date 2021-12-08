@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import me.racci.raccicore.api.utils.primitive.ColourUtils.colour
import me.racci.raccicore.api.utils.primitive.LegacyUtils
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun Player.msg(messages: Array<ComponentLike>) = messages.forEach(::sendMessage)
fun Player.msg(messages: Collection<ComponentLike>) = messages.forEach(::sendMessage)

fun CommandSender.msg(messages: Array<ComponentLike>) = messages.forEach(::sendMessage)
fun CommandSender.msg(messages: Collection<ComponentLike>) = messages.forEach(::sendMessage)

fun Player.msg(message: String) = sendMessage(message)
fun Player.msg(message: ComponentLike) = sendMessage(message)
fun CommandSender.msg(message: String) = sendMessage(message)
fun CommandSender.msg(message: ComponentLike) = sendMessage(message)

fun String.parse() = miniMessage().parse(this)
fun Array<String>.parse() = map{miniMessage().parse(it)}.toTypedArray()
fun Collection<String>.parse() = map{miniMessage().parse(it)}

fun String.parseLegacy() = LegacyUtils.parseLegacy(this)
fun Array<String>.parseLegacy() = LegacyUtils.parseLegacy(this.toList()).toTypedArray()
fun Collection<String>.parseLegacy() = LegacyUtils.parseLegacy(this.toList())

fun String.coloured() = colour(this, true)