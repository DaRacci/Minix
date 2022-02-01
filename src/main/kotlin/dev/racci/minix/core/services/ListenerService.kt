package dev.racci.minix.core.services

import dev.racci.minix.api.events.LiquidType
import dev.racci.minix.api.events.PlayerEnterLiquidEvent
import dev.racci.minix.api.events.PlayerExitLiquidEvent
import dev.racci.minix.api.events.PlayerMoveFullXYZEvent
import dev.racci.minix.api.events.PlayerMoveXYZEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.utils.kotlin.and
import dev.racci.minix.core.Minix
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

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
    }
}
