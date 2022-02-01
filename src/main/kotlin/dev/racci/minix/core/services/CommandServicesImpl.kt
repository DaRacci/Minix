package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.unregister
import dev.racci.minix.api.services.CommandServices
import dev.racci.minix.core.Minix
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent
import kotlin.reflect.KClass

internal class CommandServicesImpl(override val plugin: Minix) : Extension<Minix>(), CommandServices {

    override val name = "Command Manager"

    override val bindToKClass: KClass<*>
        get() = CommandServices::class

    override val commands = HashMap<String, MutableList<Command>>()

    override suspend fun handleEnable() {
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach { it.unregister() }
        }
    }
}
