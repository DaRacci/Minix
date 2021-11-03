package me.racci.raccicore.runnables

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.events.DayEvent
import me.racci.raccicore.events.NightEvent
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.pm
import me.racci.raccicore.utils.worlds.WorldTime.isDay
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent

class TimeRunnable(plugin: RacciPlugin) : KotlinRunnable(plugin, true, true, 20, 100), KotlinListener {

    private val timeState = HashMap<String, Boolean>()

    @EventHandler
    suspend fun onWorldLoad(event: WorldLoadEvent) = withContext(plugin.asyncDispatcher) {
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