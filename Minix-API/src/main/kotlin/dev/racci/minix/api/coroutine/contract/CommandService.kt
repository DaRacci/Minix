package dev.racci.minix.api.coroutine.contract

import dev.racci.minix.api.coroutine.SuspendingCommandExecutor
import dev.racci.minix.api.coroutine.SuspendingTabCompleter
import org.bukkit.command.PluginCommand

interface CommandService {

    /**
     * Registers a suspend command executor.
     */
    fun registerSuspendCommandExecutor(
        pluginCommand: PluginCommand,
        commandExecutor: SuspendingCommandExecutor
    )

    /**
     * Registers a suspend tab completer.
     */
    fun registerSuspendTabCompleter(
        pluginCommand: PluginCommand,
        tabCompleter: SuspendingTabCompleter
    )
}
