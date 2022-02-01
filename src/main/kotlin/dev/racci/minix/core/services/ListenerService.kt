package dev.racci.minix.core.services

import dev.racci.minix.api.events.AbstractComboEvent
import dev.racci.minix.api.events.BlockData
import dev.racci.minix.api.events.LiquidType
import dev.racci.minix.api.events.PlayerEnterLiquidEvent
import dev.racci.minix.api.events.PlayerExitLiquidEvent
import dev.racci.minix.api.events.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.PlayerMoveXYZEvent
import dev.racci.minix.api.events.PlayerShiftLeftClickEvent
import dev.racci.minix.api.events.PlayerShiftRightClickEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.classConstructor
import dev.racci.minix.api.utils.kotlin.and
import dev.racci.minix.api.utils.now
import dev.racci.minix.api.utils.tryCast
import dev.racci.minix.core.data.PlayerData
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class ListenerService(override val plugin: Minix) : Extension<Minix>() {

    override val name = "Listener Service"

    override suspend fun handleEnable() {

        event<PlayerMoveEvent>(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true,
            forceAsync = true,
        ) {
            if (!hasExplicitlyChangedPosition()) return@event
            pm.callEvent<PlayerMoveXYZEvent> {
                mapOf(
                    this[0] to player,
                    this[1] to from,
                    this[2] to to,
                )
            }
        }

        event<PlayerMoveXYZEvent>(
            priority = EventPriority.HIGH,
            ignoreCancelled = true,
        ) {
            if (!hasExplicitlyChangedBlock()) return@event
            pm.callEvent<PlayerMoveFullXYZEvent> {
                mapOf(
                    this[0] to player,
                    this[1] to from,
                    this[2] to to,
                )
            }
        }

        event<PlayerMoveFullXYZEvent>(
            priority = EventPriority.HIGH,
            ignoreCancelled = true,
        ) {
            val blocks: MutableList<LiquidType> = mutableListOf()
            for (block in from.block and to.block) {
                blocks.add(LiquidType.convert(block))
            }
            if (blocks.all { b -> b.ordinal == 2 } || blocks[0] == blocks[1]) return@event
            val type = if (blocks[0].ordinal != 2 && blocks[1] == LiquidType.NON) {
                PlayerExitLiquidEvent::class
            } else PlayerEnterLiquidEvent::class

            callEvent(type) {
                mapOf(
                    this[0] to player,
                    this[1] to blocks[0],
                    this[2] to blocks[1],
                )
            }
        }

        @Suppress("UNCHECKED_CAST") event<PlayerInteractEvent>(
            priority = EventPriority.LOW, ignoreCancelled = true, forceAsync = true
        ) {
            if (action == Action.PHYSICAL) return@event

            val bd = if (clickedBlock != null) BlockData(clickedBlock!!, blockFace) else null
            val s = if (player.isSneaking) "Shift" else return@event
            val c = if (action.isLeftClick) "Left" else "Right"

            val clazz = Class.forName("dev.racci.instance.api.events.Player${s}${c}ClickEvent") as Class<AbstractComboEvent>
            val vEvent = classConstructor(
                clazz.getConstructor(
                    Player::class.java, ItemStack::class.java, BlockData::class.java, Entity::class.java
                ),
                player, item, bd, null
            )
            pm.callEvent(vEvent)

            if (vEvent.isCancelled) cancel()
        }

        event<PlayerInteractEntityEvent>(
            priority = EventPriority.LOW, ignoreCancelled = true, forceAsync = true
        ) {
            val vEvent = if (player.isSneaking) {
                PlayerShiftRightClickEvent(
                    player, player.inventory.getItem(hand), null, rightClicked
                )
            } else return@event
            pm.callEvent(vEvent)

            if (vEvent.isCancelled) cancel()
        }

        event<EntityDamageByEntityEvent>(
            priority = EventPriority.LOW, ignoreCancelled = true, forceAsync = true
        ) {
            if (damager !is Player) return@event
            val p = damager as Player
            val vEvent = if (p.isSneaking) {
                PlayerShiftLeftClickEvent(p, p.inventory.itemInMainHand, null, entity)
            } else return@event
            pm.callEvent(vEvent)

            if (vEvent.isCancelled) cancel()
        }

        @Suppress("UNCHECKED_CAST") event<PlayerSwapHandItemsEvent>(
            priority = EventPriority.LOW, ignoreCancelled = true, forceAsync = true
        ) {
            var pd: PlayerData? = null
            PlayerService.tryCast<PlayerServiceImpl> { pd = this[player.uniqueId] }
            val now = now().epochSeconds
            val shift = if (player.isSneaking) "Shift" else ""
            val double = if (now - pd!!.lastOffhand <= 0.5) {
                pd!!.lastOffhand = now
                "Double"
            } else if (shift.isEmpty()) return@event else ""

            val clazz = Class.forName("dev.racci.instance.api.events.Player${shift}${double}OffhandEvent") as Class<AbstractComboEvent>
            val vEvent = classConstructor(
                clazz.getConstructor(
                    Player::class.java, ItemStack::class.java, ItemStack::class.java, BlockData::class.java, Entity::class.java
                ),
                player, mainHandItem, offHandItem, null, null
            )
            pm.callEvent(vEvent)

            if (vEvent.isCancelled) cancel()
        }
    }
}
