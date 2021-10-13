package me.racci.raccicore.utils


typealias BukkitEvent = org.bukkit.event.Event
typealias BukkitEventPriority = org.bukkit.event.EventPriority

val server get() = org.bukkit.Bukkit.getServer()
val console get() = org.bukkit.Bukkit.getConsoleSender()
val pm get() = org.bukkit.Bukkit.getPluginManager()

