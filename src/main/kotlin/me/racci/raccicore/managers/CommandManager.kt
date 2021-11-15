package me.racci.raccicore.managers

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.extensions.KListener
import me.racci.raccicore.extensions.event
import me.racci.raccicore.extensions.unregister
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent
import kotlin.properties.Delegates

internal object CommandManager: KListener<RacciPlugin> {
    override var plugin: RacciCore by Delegates.notNull()

    val commands = HashMap<String, MutableList<Command>>()

    fun init() {
        plugin = RacciCore.instance
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach {
                it.unregister()
            }
        }
    }


}