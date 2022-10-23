package dev.racci.minix.api.extensions

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
public fun String.parse(): Component = miniMessage().parse(this)
public fun Array<String>.parse(): Array<Component> = map { miniMessage().parse(it) }.toTypedArray()
public fun Collection<String>.parse(): List<Component> = map { miniMessage().parse(it) }

/**
 * Sends this message as a chat message to the audience of players.
 *
 * @param audience The audience of players.
 */
public infix fun Component.message(audience: Audience) { audience.sendMessage(this) }
