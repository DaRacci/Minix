@file:Suppress("unused")

package dev.racci.minix.core.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import dev.racci.minix.core.Minix
import dev.racci.minix.core.Minix.Companion.getKoin
import dev.racci.minix.core.Minix.Companion.launchAsync

@CommandAlias("minix")
class CoreCommand : BaseCommand() {

    @Default
    @CommandPermission("minix.admin")
    fun onReload() {
        launchAsync {
            getKoin().get<Minix>().reload()
        }
    }
}
