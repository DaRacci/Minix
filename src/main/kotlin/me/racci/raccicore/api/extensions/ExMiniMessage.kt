@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


/**
 * Parses a string into a component.
 *
 * @param input The input string
 * @param italic If italic should be removed automatically
 * @param builder Block to apply to the component
 * @return the output component
 * @since 0.3.0
 */
inline fun miniMessage(
    input: String,
    italic: Boolean = false,
    builder: Component.() -> Unit = {}
): Component = MiniMessage.get().parse(input).apply{if(!italic) noItalic()}.also(builder)