package me.racci.raccicore.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


/**
 * Parses a string into a component.
 *
 * @param input The input string
 * @param italic If italic should be removed automatically
 * @return the output component
 */
fun miniMessage(
    input: String,
    italic: Boolean = false,
    builder: Component.() -> Unit = {}
): Component = MiniMessage.get().parse(input).apply {if(!italic) noItalic()}