package me.racci.raccicore.core.listeners

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.withContext
import me.racci.raccicore.api.events.PlayerMoveXYZEvent
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.core.RacciCore
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerTeleportListener : KotlinListener {

    @EventHandler(priority = EventPriority.HIGH)
    suspend fun onPlayerTeleport(event: PlayerTeleportEvent) = withContext(RacciCore.instance.asyncDispatcher) {
        if(event.from.x != event.to.x
            || event.from.y != event.to.y
            || event.from.z != event.to.z) {
            pm.callEvent(PlayerMoveXYZEvent(event.player, event.from, event.to))
        }
    }
}