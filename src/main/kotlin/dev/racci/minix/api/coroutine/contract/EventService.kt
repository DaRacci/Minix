package dev.racci.minix.api.coroutine.contract

import kotlinx.coroutines.Job
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("1.0.0")
interface EventService {

    /**
     * Registers a suspend listener.
     */
    fun registerSuspendListener(listener: Listener)

    /**
     * Fires a suspending event.
     */
    fun fireSuspendingEvent(event: Event): Collection<Job>
}
