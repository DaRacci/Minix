@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import org.bukkit.command.SimpleCommandMap
import java.lang.reflect.Field

val serverCommands: SimpleCommandMap by lazy {
    server::class.java.getDeclaredField("commandMap").apply {
        isAccessible = true
    }.get(server) as SimpleCommandMap
}

val knownCommandsField: Field by lazy {
    SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply {
        isAccessible = true
    }
}
