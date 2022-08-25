@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.adventure.LazyComponentReplacement
import dev.racci.minix.api.utils.adventure.LazyStringReplacement
import dev.racci.minix.api.utils.unsafeCast
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

// private val adventure by getKoin().inject<Minix>().value.adventure

/**
 * Removes the *italic* from text.
 */
inline fun <reified T : Component> T.noItalic() = decoration(TextDecoration.ITALIC, false) as T

fun MiniMessage.lazyPlaceholder(
    input: String,
    template: Array<out Pair<String, () -> Any>>
) = deserialize(
    input,
    TagResolver.resolver(
        template.map {
            val str = it.second.toString()
            TagResolver.resolver(
                it.first,
                when (str) {
                    in components -> LazyComponentReplacement { it.second().unsafeCast() }
                    else -> LazyStringReplacement { it.second().toString() }
                }
            )
        }
    )
)

/**
 * Gets this player as an audience.
 */
// fun Player.audience() = player(uniqueId)

/**
 * Gets this command sender as an audience.
 */
// fun CommandSender.audience() = adventure.sender(this)
