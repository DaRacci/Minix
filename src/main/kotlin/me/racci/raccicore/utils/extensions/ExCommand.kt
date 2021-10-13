package me.racci.raccicore.utils.extensions

import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import java.lang.reflect.Field

private val serverCommands: SimpleCommandMap by lazy {
    val server = Bukkit.getServer()
    server::class.java.getDeclaredField("commandMap").apply {
        isAccessible = true
    }.get(server) as SimpleCommandMap
}

private val knownCommandsField: Field by lazy {
    SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply {
        isAccessible = true
    }
}