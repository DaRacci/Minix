package me.racci.raccicore.utils.extensions

import net.kyori.adventure.text.BlockNBTComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.EntityNBTComponent
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.ScoreComponent
import net.kyori.adventure.text.SelectorComponent
import net.kyori.adventure.text.StorageNBTComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration


fun blockNBT(
    builder: BlockNBTComponent.Builder.() -> Unit
) = Component.blockNBT(builder)

fun entityNBT(
    builder: EntityNBTComponent.Builder.() -> Unit
) = Component.entityNBT(builder)

fun keybind(
    builder: KeybindComponent.Builder.() -> Unit
) = Component.keybind(builder)

fun score(
    builder: ScoreComponent.Builder.() -> Unit
) = Component.score(builder)

fun selector(
    builder: SelectorComponent.Builder.() -> Unit
) = Component.selector(builder)

fun storageNBT(
    builder: StorageNBTComponent.Builder.() -> Unit
) = Component.storageNBT(builder)

fun text(
    builder: TextComponent.Builder.() -> Unit
) = Component.text(builder)

fun translatable(
    builder: TranslatableComponent.Builder.() -> Unit
) = Component.translatable(builder)

fun style(
    builder: Style.Builder.() -> Unit
) = Style.style(builder)

fun Component.noItalic()
= decoration(TextDecoration.ITALIC, false)

operator fun Component.plus(
    that: ComponentLike
) = append(that)