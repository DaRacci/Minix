package dev.racci.minix.api.utils.adventure

import net.kyori.adventure.text.minimessage.tag.PreProcess
import net.kyori.adventure.text.minimessage.tag.Tag

public class LazyStringReplacement(private val value: () -> String) : Tag, PreProcess {
    override fun value(): String = value.invoke()
}
