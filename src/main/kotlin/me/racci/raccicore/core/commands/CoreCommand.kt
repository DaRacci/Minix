package me.racci.raccicore.core.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import me.racci.raccicore.core.RacciCore
import org.bukkit.command.CommandSender

class CoreCommand: BaseCommand() {

    @Default
    @CommandPermission("raccicore.admin")
    suspend fun onReload(sender: CommandSender) {
        RacciCore.instance.reload()
    }


}