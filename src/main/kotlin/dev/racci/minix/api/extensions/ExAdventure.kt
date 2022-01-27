@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.annotations.MinixDsl
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.jetbrains.annotations.ApiStatus

/**
 * Removes the *italic* from text.
 */
@MinixDsl
@ApiStatus.AvailableSince("0.3.1")
inline fun <reified T : Component> T.noItalic() = decoration(TextDecoration.ITALIC, false) as T
