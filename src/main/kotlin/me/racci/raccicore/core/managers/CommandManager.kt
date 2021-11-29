package me.racci.raccicore.core.managers

import me.racci.raccicore.api.extensions.KListener
import me.racci.raccicore.api.extensions.event
import me.racci.raccicore.api.extensions.unregister
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.core.RacciCore
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent

class CommandManager(
    override val plugin: RacciCore,
): KListener<RacciCore>, LifecycleListener<RacciCore> {

    val commands = HashMap<String, MutableList<Command>>()

    override suspend fun onEnable() {
        INSTANCE = this
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach { it.unregister() }
        }
    }

    companion object {
        private lateinit var INSTANCE: CommandManager
        val commands get() = INSTANCE.commands
    }

}