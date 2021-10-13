 package me.racci.raccicore.listeners

import me.racci.raccicore.events.*
import me.racci.raccicore.playerManager
import me.racci.raccicore.racciCore
import me.racci.raccicore.skedule.skeduleAsync
import me.racci.raccicore.utils.extensions.KotlinListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent


 /**
  * Player combo listeners
  *
  * @constructor Create empty Player combo listeners
  */
 class PlayerComboListener : KotlinListener {

     /**
      * On interact
      *
      * @param event
      */
     @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action == Action.PHYSICAL) return

        skeduleAsync(racciCore) {
            val player = event.player
            val newEvent: KotlinEvent = when (event.action) {
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
                else -> return@skeduleAsync
            }
            Bukkit.getPluginManager().callEvent(newEvent)
        }
    }

     /**
      * On offhand
      *
      * @param event
      */
     @EventHandler
    fun onOffhand(event: PlayerSwapHandItemsEvent) {
        val playerData = playerManager.getPlayerData(event.player.uniqueId)
        val last = playerData.lastOffhand
        val now = System.currentTimeMillis()
        skeduleAsync(racciCore) {
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
}