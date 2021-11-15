package me.racci.raccicore.extensions

import me.racci.raccicore.utils.strings.LegacyUtils
import me.racci.raccicore.utils.strings.colour
import net.kyori.adventure.text.ComponentLike
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun Player.msg(messages: List<ComponentLike>)       = messages.forEach(::sendMessage)
fun Player.msg(messages: Array<ComponentLike>)      = messages.forEach(::sendMessage)
fun Player.msg(messages: Collection<ComponentLike>) = messages.forEach(::sendMessage)

fun CommandSender.msg(messages: List<ComponentLike>)       = messages.forEach(::sendMessage)
fun CommandSender.msg(messages: Array<ComponentLike>)      = messages.forEach(::sendMessage)
fun CommandSender.msg(messages: Collection<ComponentLike>) = messages.forEach(::sendMessage)

fun Player.msg(message: ComponentLike)        = sendMessage(message)
fun CommandSender.msg(message: ComponentLike) = sendMessage(message)

fun String.asComponent() = LegacyUtils.parseLegacy(this)
fun List<String>.asComponent() = LegacyUtils.parseLegacy(this)
fun Array<String>.asComponent() = LegacyUtils.parseLegacy(this.toList()).toTypedArray()
fun Collection<String>.asComponent() = LegacyUtils.parseLegacy(this.toList())

fun List<String>.asSingleComponent() = text{LegacyUtils.parseLegacy(this@asSingleComponent).forEach{this.append(it)}}
fun Array<String>.asSingleComponent() = text{LegacyUtils.parseLegacy(this@asSingleComponent.toList()).forEach{this.append(it)}}
fun Collection<String>.asSingleComponent() = text{LegacyUtils.parseLegacy(this@asSingleComponent.toList()).forEach{this.append(it)}}

fun String.coloured() = colour(this, true)