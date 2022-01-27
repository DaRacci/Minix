package dev.racci.minix.core.services

import dev.racci.minix.api.events.WorldDayEvent
import dev.racci.minix.api.events.WorldNightEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.callEvent
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.extensions.worlds
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.collections.CollectionUtils.get
import dev.racci.minix.api.utils.minecraft.WorldUtils.isDay
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent

class TimeService(override val plugin: MinixPlugin) : Extension() {

    override val name = "Time Service"

    private val timeState = HashMap<String, Boolean>()

    override suspend fun handleEnable() {

        event<WorldLoadEvent> { checkTime(world) }
        event<WorldUnloadEvent> { timeState -= world.name }

        scheduler { worlds.forEach(::checkTime) }.runAsyncTaskTimer(plugin, 15L, 20L)
    }

    private fun checkTime(world: World) {
        val isDay = isDay(world)
        val wasDay = timeState[world.name, !isDay]

        if (isDay == wasDay) return

        timeState[world.name] = isDay
        val eventKClass = if (isDay) {
            WorldDayEvent::class
        } else WorldNightEvent::class

        callEvent(eventKClass) {
            mapOf(this[0] to world)
        }
    }
}
