package me.racci.raccicore.runnables

import me.racci.raccicore.events.DayEvent
import me.racci.raccicore.events.NightEvent
import me.racci.raccicore.listeners.KotlinListener
import me.racci.raccicore.utils.pm
import me.racci.raccicore.utils.worlds.WorldTime.isDay
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.scheduler.BukkitRunnable

class TimeRunnable : BukkitRunnable(), KotlinListener {

    private val timeState = HashMap<String, Boolean>()

    @EventHandler
    fun onWorldLoad(event: WorldLoadEvent) {
        timeChecker(event.world)
    }

    private fun timeChecker(world: World) {
        val now = isDay(world)
        val last = timeState.getOrDefault(world.name, !now)
        if(now == last) return
        timeState[world.name] = now
        if(now) {
            pm.callEvent(DayEvent(world))
        } else if(!now) {
            pm.callEvent(NightEvent(world))
        }
    }

    override fun run() {
        for(world in Bukkit.getWorlds()) {
            timeChecker(world)
        }
    }

}