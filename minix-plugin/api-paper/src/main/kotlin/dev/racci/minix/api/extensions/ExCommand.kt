package dev.racci.minix.api.extensions

import org.bukkit.command.SimpleCommandMap
import java.lang.reflect.Field

public val serverCommands: SimpleCommandMap by lazy {
    server::class.java.getDeclaredField("commandMap").apply {
        isAccessible = true
    }.get(server) as SimpleCommandMap
}

public val knownCommandsField: Field by lazy {
    SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply {
        isAccessible = true
    }
}
