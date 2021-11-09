package me.racci.raccicore.listeners

import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.events.PlayerDoubleOffhandEvent
import me.racci.raccicore.events.PlayerLeftClickBlockEvent
import me.racci.raccicore.events.PlayerLeftClickEntityEvent
import me.racci.raccicore.events.PlayerLeftClickEvent
import me.racci.raccicore.events.PlayerOffhandEvent
import me.racci.raccicore.events.PlayerRightClickBlockEvent
import me.racci.raccicore.events.PlayerRightClickEntityEvent
import me.racci.raccicore.events.PlayerRightClickEvent
import me.racci.raccicore.events.PlayerShiftDoubleOffhandEvent
import me.racci.raccicore.events.PlayerShiftLeftClickBlockEvent
import me.racci.raccicore.events.PlayerShiftLeftClickEntityEvent
import me.racci.raccicore.events.PlayerShiftLeftClickEvent
import me.racci.raccicore.events.PlayerShiftOffhandEvent
import me.racci.raccicore.events.PlayerShiftRightClickBlockEvent
import me.racci.raccicore.events.PlayerShiftRightClickEntityEvent
import me.racci.raccicore.events.PlayerShiftRightClickEvent
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.extensions.pm
import me.racci.raccicore.utils.now
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent


class PlayerComboListener : KotlinListener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    suspend fun onInteract(
        event: PlayerInteractEvent,
    ) = withContext(RacciCore.asyncDispatcher) {
        if(event.action == Action.PHYSICAL) return@withContext
        val player = event.player

        val vEvent = when(event.action) {
            Action.LEFT_CLICK_AIR    -> if(player.isSneaking) PlayerShiftLeftClickEvent(player, event.item) else PlayerLeftClickEvent(player, event.item)
            Action.RIGHT_CLICK_AIR   -> if(player.isSneaking) PlayerShiftRightClickEvent(player, event.item) else PlayerRightClickEvent(player, event.item)
            Action.LEFT_CLICK_BLOCK  -> if(player.isSneaking) PlayerShiftLeftClickBlockEvent(player, event.item, event.clickedBlock!!, event.blockFace) else PlayerLeftClickBlockEvent(player, event.item, event.clickedBlock!!, event.blockFace)
            Action.RIGHT_CLICK_BLOCK -> if(player.isSneaking) PlayerShiftRightClickBlockEvent(player, event.item, event.clickedBlock!!, event.blockFace) else PlayerRightClickBlockEvent(player, event.item, event.clickedBlock!!, event.blockFace)
            else                     -> return@withContext
        }

        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    suspend fun onInteractAtEntity(
        event: PlayerInteractEntityEvent,
    ) = withContext(RacciCore.asyncDispatcher) {
        val vEvent = if(event.player.isSneaking) {
            PlayerShiftRightClickEntityEvent(event.player, event.player.inventory.getItem(event.hand), event.rightClicked)
        } else PlayerRightClickEntityEvent(event.player, event.player.inventory.getItem(event.hand), event.rightClicked)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    suspend fun onAttackEntity(
        event: EntityDamageByEntityEvent
    ) = withContext(RacciCore.asyncDispatcher) {
        if(event.damager !is Player) return@withContext
        val p = event.damager as Player
        val vEvent = if(p.isSneaking) {
            PlayerShiftLeftClickEntityEvent(p, p.inventory.itemInMainHand, event.entity)
        } else PlayerLeftClickEntityEvent(p, p.inventory.itemInMainHand, event.entity)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    suspend fun onOffhand(
        event: PlayerSwapHandItemsEvent,
    ) = withContext(RacciCore.asyncDispatcher) {
        val playerData = PlayerManager[event.player.uniqueId]
        val last = playerData.lastOffhand
        val player = event.player
        val vEvent = (if((now().epochSeconds - last) <= 5L) {
            if(player.isSneaking) {
                PlayerShiftDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerDoubleOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        } else {
            playerData.lastOffhand = now().epochSeconds
            if(player.isSneaking) {
                PlayerShiftOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
            } else PlayerOffhandEvent(player, player.inventory.itemInMainHand, player.inventory.itemInOffHand)
        })
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }
}