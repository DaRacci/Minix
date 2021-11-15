package me.racci.raccicore.core.runnables

import kotlinx.coroutines.withContext
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.events.DayEvent
import me.racci.raccicore.events.NightEvent
import me.racci.raccicore.extensions.KotlinListener
import me.racci.raccicore.extensions.pm
import me.racci.raccicore.scheduler.CoroutineRunnable
import me.racci.raccicore.utils.worlds.WorldTime.isDay
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent

class TimeRunnable : CoroutineRunnable(), KotlinListener {

    private val timeState = HashMap<String, Boolean>()

    @EventHandler
    suspend fun onWorldLoad(event: WorldLoadEvent) = withContext(RacciCore.asyncDispatcher) {
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