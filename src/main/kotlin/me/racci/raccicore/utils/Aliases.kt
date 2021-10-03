package me.racci.raccicore.utils


/**
 * Bukkit shiz
 */
typealias BukkitPlugin = org.bukkit.plugin.java.JavaPlugin
typealias BukkitSender = org.bukkit.command.CommandSender
typealias BukkitEvent = org.bukkit.event.Event
typealias BukkitListener = org.bukkit.event.Listener
typealias BukkitEventPriority = org.bukkit.event.EventPriority
typealias BukkitEventHandler = org.bukkit.event.EventHandler
typealias BukkitPlayer = org.bukkit.entity.Player

typealias KPlugin = me.racci.raccicore.RacciPlugin
typealias KEvent = me.racci.raccicore.events.KotlinEvent
typealias KListener = me.racci.raccicore.listeners.KotlinListener

val server get() = org.bukkit.Bukkit.getServer()
val console get() = org.bukkit.Bukkit.getConsoleSender()
val pm get() = org.bukkit.Bukkit.getPluginManager()

