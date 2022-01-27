package dev.racci.minix.core.services

import dev.racci.minix.api.events.LiquidType
import dev.racci.minix.api.events.PlayerEnterLiquidEvent
import dev.racci.minix.api.events.PlayerExitLiquidEvent
import dev.racci.minix.api.events.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.PlayerMoveXYZEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.isPrimaryThread
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.flow.eventFlow
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.kotlin.and
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

class ListenerService(override val plugin: MinixPlugin) : Extension() {

    override val name = "Listener Service"

    override suspend fun handleEnable() {

        eventFlow<PlayerMoveEvent>(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true,
            forceAsync = true,
        ).filter(PlayerMoveEvent::hasExplicitlyChangedPosition).onEach {
            plugin.log.debug { "Handling PlayerMoveEvent for ${it.player}; On main Thread: $isPrimaryThread" }
            pm.callEvent<PlayerMoveXYZEvent> {
                mapOf(
                    this[0] to it.player,
                    this[1] to it.from,
                    this[2] to it.to,
                )
            }
        }

        eventFlow<PlayerMoveXYZEvent>(
            priority = EventPriority.HIGH,
            ignoreCancelled = true,
        ).filter(PlayerMoveXYZEvent::hasExplicitlyChangedBlock).onEach {
            plugin.log.debug { "Handling PlayerMoveXYZEvent for ${it.player}; On main Thread: $isPrimaryThread" }
            pm.callEvent<PlayerMoveFullXYZEvent> {
                mapOf(
                    this[0] to it.player,
                    this[1] to it.from,
                    this[2] to it.to,
                )
            }
        }

        eventFlow<PlayerMoveFullXYZEvent>(
            priority = EventPriority.HIGH,
            ignoreCancelled = true,
        ).onEach {
            plugin.log.debug { "Handling PlayerMoveFullXYZEvent for ${it.player}; On main Thread: $isPrimaryThread" }
            val blocks: MutableList<LiquidType> = mutableListOf()
            for (block in it.from.block and it.to.block) {
                blocks.add(LiquidType.convert(block))
            }
            if (blocks.any { b -> b.ordinal != 2 }) return@onEach

            val type = if (blocks[0].ordinal != 2 && blocks[1] == LiquidType.NON) {
                PlayerExitLiquidEvent::class
            } else PlayerEnterLiquidEvent::class

            callEvent(type) {
                mapOf(
                    this[0] to it.player,
                    this[1] to blocks[0],
                    this[2] to blocks[1],
                )
            }
        }
    }
}
