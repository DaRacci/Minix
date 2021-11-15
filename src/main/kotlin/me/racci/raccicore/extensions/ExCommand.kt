package me.racci.raccicore.extensions

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.managers.CommandManager
import me.racci.raccicore.utils.catch
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
    val cmds = CommandManager.commands[plugin.name] ?: mutableListOf()
    cmds.add(this)
    CommandManager.commands[plugin.name] =  cmds
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