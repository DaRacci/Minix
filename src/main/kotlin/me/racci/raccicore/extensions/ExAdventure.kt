package me.racci.raccicore.extensions

import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration

/**
 * DSL [BlockNBTComponent] builder.
 *
 * @param builder the builder.
 */
fun blockNBT(
    builder: BlockNBTComponent.Builder.() -> Unit
) = Component.blockNBT(builder)

/**
 * DSL [EntityNBTComponent] builder.
 *
 * @param builder the builder.
 */
fun entityNBT(
    builder: EntityNBTComponent.Builder.() -> Unit
) = Component.entityNBT(builder)

/**
 * DSL [KeybindComponent] builder.
 *
 * @param builder the builder.
 */
fun keybind(
    builder: KeybindComponent.Builder.() -> Unit
) = Component.keybind(builder)

/**
 * DSL [ScoreComponent] builder.
 *
 * @param builder the builder.
 */
fun score(
    builder: ScoreComponent.Builder.() -> Unit
) = Component.score(builder)

/**
 * DSL [SelectorComponent] builder.
 *
 * @param builder the builder
 * @receiver
 */
fun selector(
    builder: SelectorComponent.Builder.() -> Unit
) = Component.selector(builder)

/**
 * DSL [StorageNBTComponent] builder.
 *
 * @param builder the builder.
 */
fun storageNBT(
    builder: StorageNBTComponent.Builder.() -> Unit
) = Component.storageNBT(builder)

/**
 * DSL [TextComponent] builder
 *
 * @param builder the builder.
 */
fun text(
    builder: TextComponent.Builder.() -> Unit
) = Component.text(builder)

/**
 * DSL [TranslatableComponent] builder
 *
 * @param builder the builder.
 */
fun translatable(
    builder: TranslatableComponent.Builder.() -> Unit
) = Component.translatable(builder)

/**
 * DSL [Style] builder.
 *
 * @param builder the builder.
 */
fun style(
    builder: Style.Builder.() -> Unit
) = Style.style(builder)

/**
 * Removes the *italic* from text.
 */
fun Component.noItalic()
= decoration(TextDecoration.ITALIC, false)

/**
 * Add one [ComponentLike] to another
 *
 * @param that the [ComponentLike] to attach
 */
operator fun Component.plus(
    that: ComponentLike
) = append(that)