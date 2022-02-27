package dev.racci.minix.core.services

import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.events.WorldDayEvent
import dev.racci.minix.api.events.WorldNightEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.onlinePlayers
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.extensions.ticks
import dev.racci.minix.api.extensions.worlds
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.collections.CollectionUtils.get
import dev.racci.minix.api.utils.minecraft.WorldUtils.isDay
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import kotlin.time.Duration.Companion.milliseconds

class TimeService(override val plugin: Minix) : Extension<Minix>() {

    override val name = "Time Service"

    private val timeState = HashMap<String, Boolean>()

    override suspend fun handleEnable() {

        event<WorldLoadEvent> { checkTime(world) }
        event<WorldUnloadEvent> { timeState -= world.name }

        scheduler { if (onlinePlayers.isNotEmpty()) worlds.forEach(::checkTime) }.runAsyncTaskTimer(plugin, 15.milliseconds, 5.ticks)
    }

    private fun checkTime(world: World) {
        val isDay = isDay(world)
        val wasDay = timeState[world.name, !isDay]

        if (isDay == wasDay) return

        timeState[world.name] = isDay
        val eventKClass = if (isDay) {
            WorldDayEvent::class
        } else WorldNightEvent::class

        plugin.launch {
            callEvent(eventKClass) {
                mapOf(this[0] to world)
            }
        }
    }
}
