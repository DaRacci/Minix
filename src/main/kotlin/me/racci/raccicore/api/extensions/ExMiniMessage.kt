@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.template.TemplateResolver


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
    vararg template: Pair<String, Any>,
    builder: Component.() -> Unit = {}
): Component = deserialize(input, TemplateResolver.pairs(template.toMap())).also(builder)