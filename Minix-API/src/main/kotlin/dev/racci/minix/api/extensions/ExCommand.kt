@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.CommandServices
import dev.racci.minix.api.utils.kotlin.catch
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

fun Command.register(plugin: MinixPlugin) {
    serverCommands.register(plugin.name, this)
    CommandServices.commands[plugin.name]?.add(this)
}

fun Command.unregister() {
    catch<Exception> {
        val knownCommands = knownCommandsField.get(serverCommands) as MutableMap<String, Command>
        val toRemove = ArrayList<String>()
        knownCommands
            .filter { it.value == this }
            .map(Map.Entry<String, Command>::key)
            .forEach(toRemove::add)
        toRemove.forEach(knownCommands::remove)
        CommandServices.commands.values
            .forEach { it.removeIf { i -> this === i } }
    }
}
