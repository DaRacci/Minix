@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.getKoin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

private val adventure by getKoin().inject<Minix>().value.adventure

/**
 * Removes the *italic* from text.
 */
inline fun <reified T : Component> T.noItalic() = decoration(TextDecoration.ITALIC, false) as T

/**
 * Gets this player as an audience.
 */
fun Player.audience() = adventure.player(uniqueId)

/**
 * Gets this command sender as an audience.
 */
fun CommandSender.audience() = adventure.sender(this)
