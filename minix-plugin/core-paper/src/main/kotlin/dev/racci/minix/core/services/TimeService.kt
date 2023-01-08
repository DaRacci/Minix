package dev.racci.minix.core.services

import arrow.fx.coroutines.Atomic
import com.github.benmanes.caffeine.cache.Caffeine
import dev.racci.minix.api.events.world.WorldDayEvent
import dev.racci.minix.api.events.world.WorldNightEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.extensions.worlds
import dev.racci.minix.api.utils.ticks
import dev.racci.minix.core.plugin.Minix
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Duration.Companion.milliseconds

public class TimeService internal constructor() : Extension<Minix>() {
    private val timeStates = Caffeine.newBuilder().weakKeys()
        .build<World, Atomic<Boolean>> { Atomic.unsafe(it.isDayTime) }

    override suspend fun handleEnable() {
        event<WorldLoadEvent> { checkTime(world) }

        scheduler { worlds.forEach { world -> checkTime(world) } }.runAsyncTaskTimer(plugin, 15.milliseconds, 10.ticks)
    }

    private suspend fun checkTime(world: World) {
        val isDay = world.isDayTime
        val wasDay = timeStates[world].getAndSet(isDay)

        if (isDay == wasDay) return

        val eventKClass = if (isDay) {
            WorldDayEvent::class
        } else WorldNightEvent::class

        plugin.launch { eventKClass.primaryConstructor!!.call(world).callEvent() }
    }
}
