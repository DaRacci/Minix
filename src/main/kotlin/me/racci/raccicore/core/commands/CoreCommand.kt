@file:Suppress("unused")
package me.racci.raccicore.core.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import me.racci.raccicore.core.RacciCore

class CoreCommand: BaseCommand() {

    @Default
    @CommandPermission("raccicore.admin")
    fun onReload() {
        RacciCore.launchAsync {
            RacciCore.instance.reload()
        }
    }


}