package dev.racci.minix.api.coroutine.contract

import dev.racci.minix.api.coroutine.SuspendingCommandExecutor
import dev.racci.minix.api.coroutine.SuspendingTabCompleter
import org.bukkit.command.PluginCommand
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("1.0.0")
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
