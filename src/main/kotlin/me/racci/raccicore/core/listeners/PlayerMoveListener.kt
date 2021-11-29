package me.racci.raccicore.core.listeners

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.withContext
import me.racci.raccicore.api.events.PlayerMoveXYZEvent
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.core.RacciCore
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener : KotlinListener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    suspend fun onPlayerMove(event: PlayerMoveEvent) = withContext(RacciCore.instance.asyncDispatcher) {
        if(event.from.x != event.to.x
            || event.from.y != event.to.y
            || event.from.z != event.to.z) {
            pm.callEvent(PlayerMoveXYZEvent(event.player, event.from, event.to))
        }
    }
}