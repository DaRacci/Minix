package dev.racci.minix.core.services

import com.google.common.graph.Graphs
import com.google.common.graph.MutableGraph
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.data.enums.LiquidType.Companion.liquidType
import dev.racci.minix.api.events.keybinds.ComboEvent
import dev.racci.minix.api.events.keybinds.OffhandComboEvent
import dev.racci.minix.api.events.player.PlayerLiquidEnterEvent
import dev.racci.minix.api.events.player.PlayerLiquidEvent
import dev.racci.minix.api.events.player.PlayerLiquidExitEvent
import dev.racci.minix.api.events.player.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.player.PlayerMoveXYZEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.extensions.reflection.accessWith
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.flow.eventFlow
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.ticks
import dev.racci.minix.core.plugin.Minix
import io.papermc.paper.event.block.BlockBreakBlockEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_19_R2.block.impl.CraftFluids
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.FluidLevelChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.util.BoundingBox
import org.koin.core.component.get
import org.koin.core.component.inject
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

public class ListenerService internal constructor() : Extension<Minix>() {
    private class MaybeChangingLocation(
        val loc: Location,
        val expiringTick: Int
    ) : Delayed {
        override fun compareTo(other: Delayed): Int {
            if (other is MaybeChangingLocation) {
                return expiringTick.compareTo(other.expiringTick)
            } else error("Cannot compare MaybeChangingLocation to $other")
        }

        override fun getDelay(unit: TimeUnit): Long {
            val neededTicks = expiringTick - server.currentTick
            return unit.convert(neededTicks.ticks.toJavaDuration())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            return loc == other
        }

        override fun hashCode(): Int {
            return loc.hashCode()
        }

        companion object {
            fun of(loc: Location): MaybeChangingLocation {
                return MaybeChangingLocation(loc, server.currentTick + 5)
            }
        }
    }

    private val maybeChangingBlocks = DelayQueue<MaybeChangingLocation>()
    private val liquidEventChannel = Channel<PlayerLiquidEvent>(Channel.UNLIMITED)
    private val playerService by inject<PlayerService>()

    private suspend inline fun maybeAwaitNextTick(
        startTick: Int = server.currentTick,
        tickDelay: Int = 1
    ) {
        maybeAwaitUntilTick(startTick + tickDelay)
    }

    private suspend inline fun maybeAwaitUntilTick(
        targetTick: Int
    ) {
        while (server.currentTick < targetTick) {
            delay(5.milliseconds)
        }
    }

    override suspend fun handleEnable() {
        event<PlayerJoinEvent> { playerService[player].liquidType = player.location.block.liquidType }
        event<BlockBreakEvent>(EventPriority.MONITOR, true) { maybeChangingBlocks.add(MaybeChangingLocation.of(block.location)) }
        event<BlockBreakBlockEvent>(EventPriority.MONITOR, true) { maybeChangingBlocks.add(MaybeChangingLocation.of(block.location)) }

        scheduler.runTaskTimerAsynchronously(
            plugin,
            Runnable { mutableSetOf<MaybeChangingLocation>().also(maybeChangingBlocks::drainTo) },
            0,
            1
        )

        liquidEventChannel.receiveAsFlow()
            .filterNot { event -> playerService[event.player].liquidType == event.newType }
            .conflate()
            .onEach(Event::callEvent)
            .onEach { event -> playerService[event.player].liquidType = event.newType }
            .launchIn(coroutineSession.scope + coroutineSession.context)

        eventFlow<PlayerMoveEvent>(priority = EventPriority.MONITOR, ignoreCancelled = true)
            .filter(PlayerMoveEvent::hasExplicitlyChangedPosition)
            .map { event -> PlayerMoveXYZEvent(MinixPlayer.wrapped(event.player), event.from, event.to).also(Event::callEvent) }
            .conflate()
            .filter(PlayerMoveXYZEvent::hasExplicitlyChangedBlock)
            .onEach { event -> PlayerMoveFullXYZEvent(MinixPlayer.wrapped(event.player), event.from, event.to).also(Event::callEvent) }
            .conflate()
            .mapNotNull { event ->
                for (loc in maybeChangingBlocks) {
                    val bounding = BoundingBox.of(loc.loc.block)
                    if (!bounding.contains(event.to.toVector())) continue

                    maybeAwaitUntilTick(loc.expiringTick)

                    if (!bounding.contains(event.player.location.toVector())) {
                        return@mapNotNull null // Player has changed block since we started waiting
                    } else break
                }

                deferredSync {
                    val fromLiquid = event.from.block.liquidType
                    val toLiquid = event.to.block.liquidType
                    if (fromLiquid != toLiquid) Triple(MinixPlayer.wrapped(event.player), fromLiquid, toLiquid) else null
                }.await()
            }.conflate().onEach { (player, fromLiquid, toLiquid) ->
                when (toLiquid == LiquidType.NON) {
                    true -> PlayerLiquidExitEvent(player, fromLiquid, toLiquid)
                    else -> PlayerLiquidEnterEvent(player, fromLiquid, toLiquid)
                }.also { event -> liquidEventChannel.send(event) }
            }.launchIn(coroutineSession.scope + coroutineSession.context)

        event<PlayerBucketEmptyEvent>(
            EventPriority.MONITOR,
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
            EventPriority.MONITOR,
            ignoreCancelled = true
        ) { liquidEvent(block, block.liquidType, LiquidType.NON, true) }

        // TODO -> Doesn't detect holding shift and entering a block that is currently air but going to be water from the above block being broken
        event<BlockFromToEvent>(
            EventPriority.LOWEST,
            ignoreCancelled = true
        ) {
            val oldState = toBlock.state
            val newLiquid = block.liquidType
            liquidEvent(toBlock, oldState.liquidType, newLiquid, newLiquid == LiquidType.NON)
        }

        // TODO -> Fix this
        event<FluidLevelChangeEvent>(
            EventPriority.MONITOR,
            ignoreCancelled = true
        ) {
            if (block.blockData as? CraftFluids == null || newData as? CraftFluids != null) return@event

            liquidEvent(block, block.liquidType, LiquidType.NON, true)
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
                null,
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

        @Suppress("UnstableApiUsage", "UNCHECKED_CAST", "REMOVAL", "DEPRECATION", "kotlin:S1874")
        event<PluginEnableEvent> {
            if (this.plugin.name != "eco") return@event

            SimplePluginManager::class.declaredMemberProperties
                .findKCallable("dependencyGraph")
                .map { prop -> prop.accessWith { get(pluginManager.castOrThrow()) } as MutableGraph<String> }
                .filter { graph -> Graphs.reachableNodes(graph, plugin.description.name).contains("Minix") }
                .fold(
                    ifSome = { logger.info { "Eco is patched, hopefully no linkage errors!" } },
                    ifEmpty = {
                        logger.error { "Eco doesn't appear to be patched to work with Minix, Please obtain the patch or disable all Eco plugins." }
                        pluginManager.disablePlugin(this.plugin)
                    }
                )
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
            K::class.isSubclassOf(OffhandComboEvent::class) -> arrayOf(player, item, offhandItem, bData, entity)
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
        if (previous == new) return

        val nearby = block.world.getNearbyEntities(block.boundingBox) { entity -> entity is Player && !entity.isDead } as Collection<Player>
        if (nearby.isEmpty()) return

        async {
            for (player in nearby) {
                val event = if (exiting) {
                    PlayerLiquidExitEvent(MinixPlayer.wrapped(player), previous!!, new)
                } else PlayerLiquidEnterEvent(MinixPlayer.wrapped(player), previous ?: LiquidType.NON, new)
                liquidEventChannel.send(event)
            }
        }
    }

    private companion object {
        const val KEYBIND_PREFIX = "dev.racci.minix.api.events.keybind.Player"
    }
}
