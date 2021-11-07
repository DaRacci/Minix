package me.racci.raccicore.listeners

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.events.PlayerMoveXYZEvent
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.extensions.pm
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerTeleportListener : KotlinListener {

    @EventHandler(priority = EventPriority.HIGH)
    suspend fun onPlayerTeleport(event: PlayerTeleportEvent) = withContext(RacciCore.instance.asyncDispatcher) {
        launch {if(event.from.blockX != event.to.blockX
            || event.from.blockY != event.to.blockY
            || event.from.blockZ != event.to.blockZ) {
            pm.callEvent(PlayerMoveFullXYZEvent(event.player, event.from, event.to))
        }}
        launch {if(event.from.x != event.to.x
            || event.from.y != event.to.y
            || event.from.z != event.to.z) {
            pm.callEvent(PlayerMoveXYZEvent(event.player, event.from, event.to))
        }}
    }
}