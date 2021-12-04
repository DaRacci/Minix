package me.racci.raccicore.core.runnables

import me.racci.raccicore.api.events.DayEvent
import me.racci.raccicore.api.events.NightEvent
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.scheduler.CoroutineRunnable
import me.racci.raccicore.api.utils.minecraft.WorldUtils.isDay
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent

class TimeRunnable : CoroutineRunnable(), KotlinListener {

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

    override suspend fun run() {
        for(world in Bukkit.getWorlds()) {
            timeChecker(world)
        }
    }
}