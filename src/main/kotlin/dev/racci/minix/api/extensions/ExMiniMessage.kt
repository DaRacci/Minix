@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver
import net.kyori.adventure.text.minimessage.placeholder.Replacement

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
    builder: Component.() -> Unit = {},
): Component = deserialize(input).apply { if (!italic) noItalic() }.also(builder)

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
) = deserialize(
    input,
    PlaceholderResolver.map(
        template.associate {
            it.first to (
                (it.second as? Component)?.asReplacement() ?: Replacement.miniMessage(
                    it.second as? String
                        ?: it.second.toString()
                )
                )
        }
    )
).also(builder)

// Fuck this is a hacky way of doing this shit
fun ComponentLike?.asReplacement(): Replacement<*>? = if (this == null) null else Replacement.component(this)
