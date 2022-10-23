package dev.racci.minix.api.utils.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Inserting
import net.kyori.adventure.text.minimessage.tag.Tag

public class LazyComponentReplacement(private val value: () -> Component) : Tag, Inserting {
    override fun value(): Component = value.invoke()

    override fun allowsChildren(): Boolean = false
}
