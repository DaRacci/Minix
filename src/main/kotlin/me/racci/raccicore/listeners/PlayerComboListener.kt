 package me.racci.raccicore.listeners

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.events.PlayerDoubleOffhandEvent
import me.racci.raccicore.events.PlayerLeftClickEvent
import me.racci.raccicore.events.PlayerOffhandEvent
import me.racci.raccicore.events.PlayerRightClickEvent
import me.racci.raccicore.events.PlayerShiftDoubleOffhandEvent
import me.racci.raccicore.events.PlayerShiftLeftClickEvent
import me.racci.raccicore.events.PlayerShiftOffhandEvent
import me.racci.raccicore.events.PlayerShiftRightClickEvent
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.extensions.pm
import me.racci.raccicore.utils.now
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent


 class PlayerComboListener : KotlinListener {

     @EventHandler
     suspend fun onInteract(event: PlayerInteractEvent) = withContext(RacciCore.instance.asyncDispatcher) {
         if (event.action == Action.PHYSICAL) return@withContext
         val player = event.player

         val newEvent = when(event.action) {
             Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR -> {
                 if (player.isSneaking) PlayerShiftLeftClickEvent(
                     player, event.item, event.interactionPoint,
                     event.clickedBlock, event.blockFace, null
                 ) else PlayerLeftClickEvent(
                     player, event.item, event.interactionPoint,
                     event.clickedBlock, event.blockFace, null
                 )
             }
             Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR -> {
                 if (player.isSneaking) PlayerShiftRightClickEvent(
                     player, event.item, event.interactionPoint,
                     event.clickedBlock, event.blockFace, null
                 ) else PlayerRightClickEvent(
                     player, event.item, event.interactionPoint,
                     event.clickedBlock, event.blockFace, null
                 )
             }
             else -> return@withContext
         }
         pm.callEvent(newEvent)

         if(newEvent.isCancelled) event.isCancelled = true
     }

     @EventHandler
    fun onOffhand(event: PlayerSwapHandItemsEvent) {
        val playerData = PlayerManager[event.player.uniqueId]
        val last = playerData.lastOffhand
        val player = event.player
        val vEvent = (if((now().toEpochMilliseconds() - last) <= 500L) {
            if (player.isSneaking) {
                PlayerShiftDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        } else {
            playerData.lastOffhand = now().toEpochMilliseconds()
            if (player.isSneaking) {
                PlayerShiftOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        })
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }
}