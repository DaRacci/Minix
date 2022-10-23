package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Inserting
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * Parses a string into a component.
 *
 * @param input The input string
 * @param italic If italic should be removed automatically
 * @param builder Block to apply to the component
 * @return the output component
 * @since 0.3.0
 */
public inline fun MiniMessage.parse(
    input: String,
    italic: Boolean = false,
    builder: Component.() -> Unit = {}
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
public inline fun MiniMessage.template(
    input: String,
    vararg template: Pair<String, *>,
    builder: Component.() -> Unit = {}
): Component = deserialize(
    input,
    TagResolver.resolver(
        template.map {
            TagResolver.resolver(
                it.first,
                (it.second as? Component)?.asInsert() ?: Tag.preProcessParsed(it.second.toString())
            )
        }
    )
).also(builder)

@PublishedApi
internal fun Component?.asInsert(): Inserting? = if (this == null) null else Inserting { this }
