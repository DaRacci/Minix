package dev.racci.minix.api.services

import dev.racci.minix.api.utils.getKoin
import org.bukkit.command.Command

interface CommandServices {

    val commands: HashMap<String, MutableList<Command>>

    companion object : CommandServices by getKoin().get()
}
