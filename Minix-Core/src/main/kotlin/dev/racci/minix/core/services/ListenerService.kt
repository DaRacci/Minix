package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.coroutineService
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.events.AbstractComboEvent
import dev.racci.minix.api.events.BlockData
import dev.racci.minix.api.events.LiquidType
import dev.racci.minix.api.events.LiquidType.Companion.liquidType
import dev.racci.minix.api.events.PlayerEnterLiquidEvent
import dev.racci.minix.api.events.PlayerExitLiquidEvent
import dev.racci.minix.api.events.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.PlayerMoveXYZEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.classConstructor
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.unsafeCast
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_19_R1.block.impl.CraftFluids
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.FluidLevelChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

@MappedExtension(Minix::class, "Listener Service")
class ListenerService(override val plugin: Minix) : Extension<Minix>() {

    override suspend fun handleEnable() {

        event<PlayerMoveEvent>(
            EventPriority.HIGHEST,
            ignoreCancelled = true,
            forceAsync = true,
        ) {
            if (!hasExplicitlyChangedPosition()) return@event

            PlayerMoveXYZEvent(player, from, to).callEvent()
        }

        event<PlayerMoveXYZEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true,
        ) {
            if (!hasExplicitlyChangedBlock()) return@event

            PlayerMoveFullXYZEvent(player, from, to).callEvent()
        }

        event<PlayerMoveFullXYZEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true,
        ) {
            val fromLiquid = from.block.liquidType
            val toLiquid = to.block.liquidType
            if (fromLiquid == toLiquid ||
                fromLiquid == LiquidType.NON &&
                toLiquid == LiquidType.NON
            ) return@event

            val type = if (fromLiquid != LiquidType.NON && toLiquid == LiquidType.NON) {
                PlayerExitLiquidEvent::class
            } else PlayerEnterLiquidEvent::class

            callEvent(type) {
                mapOf(
                    this[0] to player,
                    this[1] to fromLiquid,
                    this[2] to toLiquid,
                )
            }
        }

        event<PlayerBucketEmptyEvent>(
            EventPriority.HIGHEST,
            ignoreCancelled = true,
        ) {
            val from = block.liquidType
            val to = bucket.liquidType
            if (from == LiquidType.NON &&
                to == LiquidType.WATER &&
                player.world.environment == World.Environment.NETHER
            ) return@event

            liquidEvent(block, block.liquidType, bucket.liquidType, false)
        }

        event<PlayerBucketFillEvent>(
            EventPriority.HIGHEST,
            ignoreCancelled = true,
        ) {
            liquidEvent(block, block.liquidType, LiquidType.NON, true)
        }

        event<BlockFromToEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true,
        ) {
            val new = block.liquidType
            liquidEvent(toBlock, toBlock.liquidType, new, new == LiquidType.NON)
        }

        event<FluidLevelChangeEvent>(
            EventPriority.MONITOR,
            ignoreCancelled = true,
        ) {
            if (block.blockData as? CraftFluids == null || newData as? CraftFluids != null) return@event

            liquidEvent(block, block.liquidType, LiquidType.NON, true)
        }

        event<PlayerEnterLiquidEvent> {
            log.debug { "Player ${player.name} is entering liquid ${newType.name} from the previous ${previousType.name}" }
        }

        event<PlayerExitLiquidEvent> {
            log.debug { "Player ${player.name} is exiting liquid ${newType.name} from the previous ${previousType.name}" }
        }

        event<PlayerInteractEvent>(
            EventPriority.LOW,
            forceAsync = true
        ) {
            if (action == Action.PHYSICAL ||
                hand == EquipmentSlot.OFF_HAND &&
                player.inventory.itemInOffHand.type == Material.AIR
            ) return@event

            val bd = if (clickedBlock != null) BlockData(clickedBlock!!, blockFace) else null
            val shift = if (player.isSneaking) "Shift" else ""
            val (click, double) = if (action.isLeftClick) {
                "Left" to if (PlayerServiceImpl.getService()[player.uniqueId].isDoubleAttack) "Double" else ""
            } else "Right" to if (PlayerServiceImpl.getService()[player.uniqueId].isDoubleInteract) "Double" else ""
            val clazz = Class.forName("dev.racci.minix.api.events.Player$shift$double${click}ClickEvent").unsafeCast<Class<AbstractComboEvent>>()

            classConstructor(
                clazz.getConstructor(Player::class.java, ItemStack::class.java, BlockData::class.java, Entity::class.java),
                player,
                item,
                bd,
                null
            ).callEvent().ifTrue(::cancel)
        }

        event<PlayerInteractEntityEvent>(
            EventPriority.LOW,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            val shift = if (player.isSneaking) "Shift" else ""
            val double = if (PlayerServiceImpl.getService()[player.uniqueId].isDoubleInteract) "Double" else ""
            val clazz = Class.forName("dev.racci.minix.api.events.Player${shift}${double}RightClickEvent").unsafeCast<Class<AbstractComboEvent>>()

            classConstructor(
                clazz.getConstructor(
                    Player::class.java,
                    ItemStack::class.java,
                    BlockData::class.java,
                    Entity::class.java
                ),
                player, player.inventory.getItem(hand), null, rightClicked
            ).callEvent().ifTrue(::cancel)
        }

        event<EntityDamageByEntityEvent>(
            EventPriority.LOW,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            val p = damager as? Player ?: return@event
            val shift = if (p.isSneaking) "Shift" else ""
            val double = if (PlayerServiceImpl.getService()[p.uniqueId].isDoubleAttack) "Double" else ""
            val clazz = Class.forName("dev.racci.minix.api.events.Player${shift}${double}LeftClickEvent").unsafeCast<Class<AbstractComboEvent>>()

            classConstructor(
                clazz.getConstructor(
                    Player::class.java,
                    ItemStack::class.java,
                    BlockData::class.java,
                    Entity::class.java
                ),
                p, p.inventory.itemInMainHand, null, entity
            ).callEvent().ifTrue(::cancel)
        }

        event<PlayerSwapHandItemsEvent>(
            EventPriority.LOW,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            val shift = if (player.isSneaking) "Shift" else ""
            val double = if (PlayerServiceImpl.getService()[player.uniqueId].isDoubleOffhand) "Double" else ""
            val clazz = Class.forName("dev.racci.minix.api.events.Player${shift}${double}OffhandEvent").unsafeCast<Class<AbstractComboEvent>>()

            classConstructor(
                clazz.getConstructor(
                    Player::class.java,
                    ItemStack::class.java,
                    ItemStack::class.java,
                    BlockData::class.java,
                    Entity::class.java
                ),
                player, mainHandItem, offHandItem, null, null
            ).callEvent().ifTrue(::cancel)
        }

        event<PluginDisableEvent> {
            val minixPlugin = this.plugin as? MinixPlugin ?: return@event
            coroutineService.disable(minixPlugin)
        }
    }

    // Maybe some sort of cache?
    private fun liquidEvent(
        block: Block,
        previous: LiquidType?,
        new: LiquidType,
        exiting: Boolean
    ) {
        if (previous == LiquidType.NON && new == LiquidType.NON || previous == new) return

        val nearby = block.location
            .toCenterLocation()
            .getNearbyEntitiesByType(Player::class.java, 0.5, 0.5) {
                !it.isDead || it.location.block.liquidType == new || PlayerServiceImpl.getService()[it.uniqueId].liquidType == new // Don't trigger if the player is dead or already in that liquid type
            }.takeUnless(Collection<*>::isEmpty) ?: return

        plugin.launchAsync {
            for (player in nearby) {
                val cancelled = if (exiting) {
                    PlayerExitLiquidEvent(player, previous!!, new).callEvent()
                } else PlayerEnterLiquidEvent(player, previous ?: LiquidType.NON, new).callEvent()

                if (cancelled) continue
                PlayerServiceImpl.getService()[player.uniqueId].liquidType = new
            }
        }
    }
}
