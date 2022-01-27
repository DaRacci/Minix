package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.coroutine.SuspendingCommandExecutor
import dev.racci.minix.api.coroutine.SuspendingTabCompleter
import dev.racci.minix.api.coroutine.contract.CommandService
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin

internal class CommandServiceImpl(
    private val plugin: Plugin,
    private val coroutineSession: CoroutineSession,
) : CommandService {

    /**
     * Registers a suspend command executor.
     */
    override fun registerSuspendCommandExecutor(
        pluginCommand: PluginCommand,
        commandExecutor: SuspendingCommandExecutor,
    ) {
        pluginCommand.setExecutor { p0, p1, p2, p3 ->
            // If the result is delayed we can automatically assume it is true.
            var success = true

            coroutineSession.launch(coroutineSession.dispatcherMinecraft) {
                success = commandExecutor.onCommand(p0, p1, p2, p3)
            }

            success
        }
    }

    /**
     * Registers a suspend tab completer.
     */
    override fun registerSuspendTabCompleter(
        pluginCommand: PluginCommand,
        tabCompleter: SuspendingTabCompleter,
    ) {
        pluginCommand.setTabCompleter { sender, command, alias, args ->
            var result = emptyList<String>()

            coroutineSession.launch(coroutineSession.dispatcherMinecraft) {
                result = tabCompleter.onTabComplete(sender, command, alias, args)
            }

            result
        }
    }
}
