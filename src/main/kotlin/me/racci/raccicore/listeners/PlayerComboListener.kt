 package me.racci.raccicore.listeners

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.events.*
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.pm
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent


 class PlayerComboListener : KotlinListener {

     @EventHandler
     suspend fun onInteract(event: PlayerInteractEvent) = withContext(RacciCore.instance.asyncDispatcher) {
         if (event.action == Action.PHYSICAL) return@withContext
         val player = event.player

         pm.callEvent(when(event.action) {
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
         })
     }

     @EventHandler
    suspend fun onOffhand(event: PlayerSwapHandItemsEvent) = withContext(RacciCore.instance.asyncDispatcher) {
        val playerData = PlayerManager[event.player.uniqueId]
        val last = playerData.lastOffhand
        val now = System.currentTimeMillis()
        val player = event.player
        Bukkit.getPluginManager().callEvent(if((now - last) <= 500L) {
            if (player.isSneaking) {
                PlayerShiftDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        } else {
            playerData.lastOffhand = System.currentTimeMillis()
            if (player.isSneaking) {
                PlayerShiftOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        })
    }
}