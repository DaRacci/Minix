@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver


/**
 * Parses a string into a component.
 *
 * @param input The input string
 * @param italic If italic should be removed automatically
 * @param builder Block to apply to the component
 * @return the output component
 * @since 0.3.0
 */
inline fun MiniMessage.parse(
    input: String,
    italic: Boolean = false,
    builder: Component.() -> Unit = {}
): Component = parse(input).apply{if(!italic) noItalic()}.also(builder)

/**
 * Parses a string into a component.
 *
 * @param input The input string
 * @param template Template pairs to resolve
 * @param builder Block to apply to the component
 * @return the output component
 * @since 0.3.1
 */
inline fun MiniMessage.template(
    input: String,
    vararg template: Pair<String, *>,
    builder: Component.() -> Unit = {}
): Component = deserialize(input, PlaceholderResolver.pairs(template.associate{it.first to (it.second as? Component ?: it.second as? String ?: it.second.toString())})).also(builder)