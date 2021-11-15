package me.racci.raccicore.core.managers

import me.racci.raccicore.core.Provider
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.extensions.KListener
import me.racci.raccicore.extensions.event
import me.racci.raccicore.extensions.unregister
import me.racci.raccicore.lifecycle.LifecycleListener
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent

class CommandManager(
    override val plugin: RacciCore,
): KListener<RacciCore>, LifecycleListener<RacciCore> {

    override suspend fun onEnable() {
        Provider.commandManager = this
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach { it.unregister() }
        }
    }

    companion object {
        val commands = HashMap<String, MutableList<Command>>()
    }

}