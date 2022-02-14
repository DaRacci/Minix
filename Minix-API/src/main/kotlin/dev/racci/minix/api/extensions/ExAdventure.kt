@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

/**
 * Removes the *italic* from text.
 */
inline fun <reified T : Component> T.noItalic() = decoration(TextDecoration.ITALIC, false) as T
