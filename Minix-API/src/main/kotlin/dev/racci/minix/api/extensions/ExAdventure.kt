@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

// private val adventure by getKoin().inject<Minix>().value.adventure

/**
 * Removes the *italic* from text.
 */
inline fun <reified T : Component> T.noItalic() = decoration(TextDecoration.ITALIC, false) as T

/**
 * Gets this player as an audience.
 */
// fun Player.audience() = player(uniqueId)

/**
 * Gets this command sender as an audience.
 */
// fun CommandSender.audience() = adventure.sender(this)
