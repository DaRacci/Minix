package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.events.WorldDayEvent
import dev.racci.minix.api.events.WorldNightEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.onlinePlayers
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.extensions.ticks
import dev.racci.minix.api.extensions.world
import dev.racci.minix.api.extensions.worlds
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.collections.CollectionUtils.cacheOf
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Duration.Companion.milliseconds

@MappedExtension(Minix::class, "Time Service")
class TimeService(override val plugin: Minix) : Extension<Minix>() {

    private val timeState = cacheOf<String, AtomicBoolean> { atomic(world(this)!!.isDayTime) }

    override suspend fun handleEnable() {
        event<WorldLoadEvent> { checkTime(world) }
        event<WorldUnloadEvent> { timeState.invalidate(world.name) }

        scheduler { if (onlinePlayers.isNotEmpty()) worlds.forEach(::checkTime) }.runAsyncTaskTimer(plugin, 15.milliseconds, 10.ticks)
    }

    private fun checkTime(world: World) {
        val isDay = world.isDayTime
        val wasDay = timeState[world.name].getAndSet(isDay)

        if (isDay == wasDay) return

        val eventKClass = if (isDay) {
            WorldDayEvent::class
        } else WorldNightEvent::class

        plugin.launch { eventKClass.primaryConstructor!!.call(world).callEvent() }
    }
}
