@file:Suppress("unused")
@file:JvmName("Logger")
package me.racci.raccilib

import org.bukkit.Bukkit
import org.bukkit.ChatColor

fun log(level: Level, message: String) {
    when(level) {
        Level.ERROR -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&4[&c&lERROR&4] &f$message", true)!!)
        Level.WARNING -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&e[&6&lWARNING&e] &f$message", true)!!)
        Level.INFO -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&8[&e&lINFO&8] &f$message", true)!!)
        Level.DEBUG -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&7[&f&lDEBUG&7] &f$message", true)!!)
        Level.SUCCESS -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&2[&a&lSUCCESS&2] &f$message", true)!!)
        Level.OUTLINE -> Bukkit.getConsoleSender().sendMessage(ChatColor.color("&7$message", true)!!)
    }
}

enum class Level { ERROR, WARNING, INFO, DEBUG, SUCCESS, OUTLINE }