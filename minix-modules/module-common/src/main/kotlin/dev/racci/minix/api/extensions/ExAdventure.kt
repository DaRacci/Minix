package dev.racci.minix.api.extensions

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.adventure.LazyComponentReplacement
import dev.racci.minix.api.utils.adventure.LazyStringReplacement
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

// This is so we don't have to worry about shading etc.
private val components by lazy {
    val parent = Component::class.qualifiedName!!.split(".").dropLast(1).joinToString(".")
    val path = "() -> $parent"
    persistentListOf(
        "$path.Component",
        "$path.MiniMessage",
        "$path.TextComponent",
        "$path.KeybindComponent",
        "$path.TranslatableComponent"
    )
}

/**
 * Removes the *italic* from text.
 */
public inline fun <reified T : Component> T.noItalic(): T = decoration(TextDecoration.ITALIC, false) as T

public fun MiniMessage.lazyPlaceholder(
    input: String,
    template: Array<out Pair<String, () -> Any>>
): Component = deserialize(
    input,
    TagResolver.resolver(
        template.map { (placeholder, value) ->
            val str = value.toString()
            TagResolver.resolver(
                placeholder,
                when (str) {
                    in components -> LazyComponentReplacement(value::castOrThrow)
                    else -> LazyStringReplacement(value::toString)
                }
            )
        }
    )
)
