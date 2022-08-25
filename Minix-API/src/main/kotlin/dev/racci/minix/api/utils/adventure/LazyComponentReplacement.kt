package dev.racci.minix.api.utils.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Inserting
import net.kyori.adventure.text.minimessage.tag.Tag

class LazyComponentReplacement(private val value: () -> Component) : Tag, Inserting {
    override fun value() = value.invoke()

    override fun allowsChildren() = false
}
