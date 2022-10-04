package dev.racci.minix.core.services

import com.google.common.graph.Graphs
import com.google.common.graph.MutableGraph
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.data.enums.LiquidType.Companion.liquidType
import dev.racci.minix.api.events.keybind.ComboEvent
import dev.racci.minix.api.events.keybind.OffhandComboEvent
import dev.racci.minix.api.events.player.PlayerLiquidEnterEvent
import dev.racci.minix.api.events.player.PlayerLiquidExitEvent
import dev.racci.minix.api.events.player.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.player.PlayerMoveXYZEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.log
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.accessReturn
import dev.racci.minix.api.utils.collections.CollectionUtils.first
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
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.SimplePluginManager
import org.koin.core.component.get
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

@MappedExtension(Minix::class, "Listener Service")
class ListenerService(override val plugin: Minix) : Extension<Minix>() {

    override suspend fun handleEnable() {
        event<PlayerMoveEvent>(
            EventPriority.HIGHEST,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            if (!hasExplicitlyChangedPosition()) return@event

            PlayerMoveXYZEvent(player, from, to).callEvent()
        }

        event<PlayerMoveXYZEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true
        ) {
            if (!hasExplicitlyChangedBlock()) return@event

            PlayerMoveFullXYZEvent(player, from, to).callEvent()
        }

        event<PlayerMoveFullXYZEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true
        ) {
            val fromLiquid = from.block.liquidType
            val toLiquid = to.block.liquidType
            if (fromLiquid == toLiquid ||
                fromLiquid == LiquidType.NON &&
                toLiquid == LiquidType.NON
            ) return@event

            val type = if (fromLiquid != LiquidType.NON && toLiquid == LiquidType.NON) {
                PlayerLiquidExitEvent::class
            } else PlayerLiquidEnterEvent::class

            callEvent(type) {
                mapOf(
                    this[0] to player,
                    this[1] to fromLiquid,
                    this[2] to toLiquid
                )
            }
        }

        event<PlayerBucketEmptyEvent>(
            EventPriority.HIGHEST,
            ignoreCancelled = true
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
            ignoreCancelled = true
        ) {
            liquidEvent(block, block.liquidType, LiquidType.NON, true)
        }

        event<BlockFromToEvent>(
            EventPriority.HIGH,
            ignoreCancelled = true
        ) {
            val new = block.liquidType
            liquidEvent(toBlock, toBlock.liquidType, new, new == LiquidType.NON)
        }

        event<FluidLevelChangeEvent>(
            EventPriority.MONITOR,
            ignoreCancelled = true
        ) {
            if (block.blockData as? CraftFluids == null || newData as? CraftFluids != null) return@event

            liquidEvent(block, block.liquidType, LiquidType.NON, true)
        }

        event<PlayerLiquidEnterEvent> {
            log.debug { "Player ${player.name} is entering liquid ${newType.name} from the previous ${previousType.name}" }
        }

        event<PlayerLiquidExitEvent> {
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

            getComboEvent<ComboEvent>(
                action,
                player,
                item,
                null,
                clickedBlock?.let { ComboEvent.BlockData(it, blockFace) },
                null
            ).callEvent().ifTrue(::cancel)
        }

        event<PlayerInteractEntityEvent>(
            EventPriority.LOW,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            getComboEvent<ComboEvent>(
                Action.RIGHT_CLICK_AIR,
                player,
                player.inventory.itemInMainHand,
                null,
                null,
                rightClicked
            ).callEvent().ifTrue(::cancel)
        }

        event<EntityDamageByEntityEvent>(
            EventPriority.MONITOR,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            val player = damager as? Player ?: return@event
            getComboEvent<ComboEvent>(
                Action.LEFT_CLICK_AIR,
                player,
                player.inventory.itemInMainHand,
                null,
                null,
                entity
            ).callEvent().ifTrue(::cancel)
        }

        event<PlayerSwapHandItemsEvent>(
            EventPriority.MONITOR,
            ignoreCancelled = true,
            forceAsync = true
        ) {
            val rayTraceResult = player.rayTraceBlocks(16.0)
            getComboEvent<OffhandComboEvent>(
                Action.LEFT_CLICK_AIR,
                player,
                player.inventory.itemInMainHand,
                player.inventory.itemInOffHand,
                rayTraceResult?.hitBlock?.let { ComboEvent.BlockData(it, rayTraceResult.hitBlockFace!!) },
                rayTraceResult?.hitEntity
            ).callEvent().ifTrue {
                val item = player.inventory.itemInMainHand
                player.inventory.setItemInMainHand(player.inventory.itemInOffHand)
                player.inventory.setItemInOffHand(item)
            }
        }

        @Suppress("UnstableApiUsage")
        event<PluginEnableEvent> {
            if (this.plugin.name != "eco") return@event

            val graph = SimplePluginManager::class.declaredMemberProperties
                .first("dependencyGraph")
                .accessReturn { get(pluginManager.unsafeCast()) } as MutableGraph<String>
            val patched = Graphs.reachableNodes(graph, this.plugin.description.name).contains("Minix")

            if (!patched) {
                logger.error { "Eco doesn't appear to be patched to work with Minix, Please obtain the patch or disable all Eco plugins." }
                pluginManager.disablePlugin(this.plugin)
            }

            logger.info { "Eco is patched, hopefully no linkage errors!" }
        }
    }

    private inline fun <reified K : ComboEvent> getComboEvent(
        action: Action?,
        player: Player,
        item: ItemStack?,
        offhandItem: ItemStack?,
        bData: ComboEvent.BlockData?,
        entity: Entity?
    ): K {
        val args = when {
            K::class.java.isAssignableFrom(ComboEvent::class.java) -> arrayOf(player, item, offhandItem, bData, entity)
            else -> arrayOf(player, item, bData, entity)
        }

        val (button, double) = when {
            action == null -> "Offhand" to get<PlayerService>()[player].isDoubleOffhand
            action.isLeftClick -> "Primary" to get<PlayerService>()[player].isDoubleAttack
            else -> "Secondary" to get<PlayerService>()[player].isDoubleInteract
        }

        val classString = buildString {
            append(KEYBIND_PREFIX)
            if (player.isSneaking) append("Sneak")
            if (double) append("Double")
            append(button)
            append("Event")
        }

        val kClass = Class.forName(classString).kotlin.castOrThrow<KClass<K>>()
        return kClass.primaryConstructor!!.call(*args)
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

    private companion object {
        const val KEYBIND_PREFIX = "dev.racci.minix.api.events.keybind.Player"
    }
}
