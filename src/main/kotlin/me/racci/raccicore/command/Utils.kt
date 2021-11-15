package me.racci.raccicore.command

import me.racci.raccicore.command.Failure.failure
import me.racci.raccicore.extensions.asComponent
import me.racci.raccicore.extensions.msg
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object Utils {

    const val SEND_SUB_COMMANDS_LABEL_PLACEHOLDER = "{label}"
    const val SEND_SUB_COMMANDS_NAME_PLACEHOLDER = "{subcmd}"
    const val SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER = "{description}"

    val SEND_SUB_COMMANDS_DEFAULT_FORMAT = "${ChatColor.BLUE}/$SEND_SUB_COMMANDS_LABEL_PLACEHOLDER ${ChatColor.YELLOW}$SEND_SUB_COMMANDS_NAME_PLACEHOLDER ${ChatColor.BLUE}-> ${ChatColor.GRAY}$SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER"

    val Executor<Player>.player: Player get() = sender

    fun Executor<*>.sendSubCommandsList(
        format: String = SEND_SUB_COMMANDS_DEFAULT_FORMAT,
        needCommandPermission: Boolean = true
    ) {
        val subcommands = command.subCommands.filterNot {
            needCommandPermission && !sender.hasPermission(it.permission!!)
        }.associateWith {
            format.replace(SEND_SUB_COMMANDS_LABEL_PLACEHOLDER, label, true)
                .replace(SEND_SUB_COMMANDS_NAME_PLACEHOLDER, it.name, true)
                .replace(SEND_SUB_COMMANDS_DESCRIPTION_PLACEHOLDER, it.description, true)
        }
        if(subcommands.isEmpty()) failure(command.permissionMessage())
        if(sender is Player) {
            subcommands.map {
                it.value.asComponent().clickEvent(ClickEvent.suggestCommand("/$label ${it.key.name}"))
            }.forEach {sender.msg(it)}
        }


    }


}