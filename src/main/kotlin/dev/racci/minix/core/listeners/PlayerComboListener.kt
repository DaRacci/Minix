@file:Suppress("UNCHECKED_CAST")

package dev.racci.minix.core.listeners

import dev.racci.minix.api.annotations.RunAsync
import dev.racci.minix.api.events.AbstractComboEvent
import dev.racci.minix.api.events.BlockData
import dev.racci.minix.api.events.PlayerShiftLeftClickEvent
import dev.racci.minix.api.events.PlayerShiftRightClickEvent
import dev.racci.minix.api.extensions.KotlinListener
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.classConstructor
import dev.racci.minix.api.utils.now
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

    @RunAsync
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun onInteract(
        event: PlayerInteractEvent,
    ) {
        if (event.action == Action.PHYSICAL) return

        val bd = if (event.clickedBlock != null) BlockData(event.clickedBlock!!, event.blockFace) else null
        val s = if (event.player.isSneaking) "Shift" else return
        val c = if (event.action.isLeftClick) "Left" else "Right"

        val clazz = Class.forName("dev.racci.instance.api.events.Player${s}${c}ClickEvent") as Class<AbstractComboEvent>
        val vEvent = classConstructor(
            clazz.getConstructor(
                Player::class.java,
                ItemStack::class.java,
                BlockData::class.java,
                Entity::class.java
            ),
            event.player, event.item, bd, null
        )
        pm.callEvent(vEvent)

        if (vEvent.isCancelled) event.isCancelled = true
    }

    @RunAsync
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onInteractAtEntity(
        event: PlayerInteractEntityEvent,
    ) {
        val vEvent = if (event.player.isSneaking) {
            PlayerShiftRightClickEvent(
                event.player,
                event.player.inventory.getItem(event.hand),
                null,
                event.rightClicked
            )
        } else return
        pm.callEvent(vEvent)

        if (vEvent.isCancelled) event.isCancelled = true
    }

    @RunAsync
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onAttackEntity(
        event: EntityDamageByEntityEvent,
    ) {
        if (event.damager !is Player) return
        val p = event.damager as Player
        val vEvent = if (p.isSneaking) {
            PlayerShiftLeftClickEvent(p, p.inventory.itemInMainHand, null, event.entity)
        } else return
        pm.callEvent(vEvent)

        if (vEvent.isCancelled) event.isCancelled = true
    }

    @RunAsync
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onOffhand(
        event: PlayerSwapHandItemsEvent,
    ) {

        val pd = PlayerService[event.player.uniqueId]
        val now = now().epochSeconds
        val shift = if (event.player.isSneaking) "Shift" else ""
        val double = if (now - pd.lastOffhand <= 0.5) {
            pd.lastOffhand = now
            "Double"
        } else if (shift.isEmpty()) return else ""

        val clazz =
            Class.forName("dev.racci.instance.api.events.Player${shift}${double}OffhandEvent") as Class<AbstractComboEvent>
        val vEvent = classConstructor(
            clazz.getConstructor(
                Player::class.java,
                ItemStack::class.java,
                ItemStack::class.java,
                BlockData::class.java,
                Entity::class.java
            ),
            event.player, event.mainHandItem, event.offHandItem, null, null
        )
        pm.callEvent(vEvent)

        if (vEvent.isCancelled) event.isCancelled = true
    }
}
