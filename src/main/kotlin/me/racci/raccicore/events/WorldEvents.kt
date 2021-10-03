package me.racci.raccicore.events

import org.bukkit.World

class NightEvent(val world: World) : KotlinEvent(true) { }

class DayEvent(val world: World) : KotlinEvent(true) { }