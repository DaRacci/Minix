package me.racci.raccicore.listeners

import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.events.AbstractComboEvent
import me.racci.raccicore.events.BlockData
import me.racci.raccicore.events.PlayerLeftClickEvent
import me.racci.raccicore.events.PlayerRightClickEvent
import me.racci.raccicore.events.PlayerShiftLeftClickEvent
import me.racci.raccicore.events.PlayerShiftRightClickEvent
import me.racci.raccicore.utils.ClassUtils
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.extensions.pm
import me.racci.raccicore.utils.now
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack


class PlayerComboListener : KotlinListener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    suspend fun onInteract(
        event: PlayerInteractEvent,
    ) = withContext(RacciCore.asyncDispatcher) {
        if(event.action == Action.PHYSICAL) return@withContext

        val bd = if(event.clickedBlock != null) BlockData(event.clickedBlock!!, event.blockFace) else null
        val s = if(event.player.isSneaking) "Shift" else ""
        val c = if(event.action.isLeftClick) "Left" else "Right"

        val clazz = Class.forName("me.racci.raccicore.events.Player${s}${c}ClickEvent") as Class<AbstractComboEvent>
        val vEvent = ClassUtils.classConstructor(
            clazz.getConstructor(
                Player::class.java,
                ItemStack::class.java,
                BlockData::class.java,
                Entity::class.java
            ), event.player, event.item, bd, null)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    suspend fun onInteractAtEntity(
        event: PlayerInteractEntityEvent,
    ) = withContext(RacciCore.asyncDispatcher) {
        val vEvent = if(event.player.isSneaking) {
            PlayerShiftRightClickEvent(event.player, event.player.inventory.getItem(event.hand), null, event.rightClicked)
        } else PlayerRightClickEvent(event.player, event.player.inventory.getItem(event.hand), null, event.rightClicked)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    suspend fun onAttackEntity(
        event: EntityDamageByEntityEvent
    ) = withContext(RacciCore.asyncDispatcher) {
        if(event.damager !is Player) return@withContext
        val p = event.damager as Player
        val vEvent = if(p.isSneaking) {
            PlayerShiftLeftClickEvent(p, p.inventory.itemInMainHand, null, event.entity)
        } else PlayerLeftClickEvent(p, p.inventory.itemInMainHand, null, event.entity)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    suspend fun onOffhand(
        event: PlayerSwapHandItemsEvent,
    ) = withContext(RacciCore.asyncDispatcher) {

        val pd = PlayerManager[event.player.uniqueId]
        val n = now().epochSeconds
        val s = if(event.player.isSneaking) "Shift" else ""
        val db = if(n - pd.lastOffhand <= 0.5) {
            pd.lastOffhand = n
            "Double"
        } else ""

        val clazz = Class.forName("me.racci.raccicore.events.Player${s}${db}OffhandEvent") as Class<AbstractComboEvent>
        val vEvent = ClassUtils.classConstructor(
            clazz.getConstructor(
                Player::class.java,
                ItemStack::class.java,
                ItemStack::class.java,
                BlockData::class.java,
                Entity::class.java
            ), event.player, event.mainHandItem, event.offHandItem, null, null)
        pm.callEvent(vEvent)

        if(vEvent.isCancelled) event.isCancelled = true
    }
}