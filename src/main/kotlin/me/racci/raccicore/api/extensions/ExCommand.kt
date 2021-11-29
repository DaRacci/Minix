@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.api.utils.catch
import me.racci.raccicore.core.managers.CommandManager
import org.bukkit.command.Command
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

fun Command.register(plugin: RacciPlugin) {
    serverCommands.register(plugin.name, this)
    val commands = CommandManager.commands[plugin.name] ?: mutableListOf()
    commands.add(this)
    CommandManager.commands[plugin.name] =  commands
}

fun Command.unregister() {
    catch<Exception> {
        val knownCommands = knownCommandsField.get(serverCommands) as MutableMap<String, Command>
        val toRemove = ArrayList<String>()
        knownCommands
            .filter{it.value == this}
            .map(Map.Entry<String, Command>::key)
            .forEach(toRemove::add)
        toRemove.forEach(knownCommands::remove)
        CommandManager.commands.values
            .forEach{it.removeIf{i->this === i}}
    }
}