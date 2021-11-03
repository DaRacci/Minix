package me.racci.raccicore.skedule

import org.jetbrains.annotations.ApiStatus

/**
 * Represents a synchronization context that a BukkitScheduler coroutine is currently in.
 */
@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
enum class SynchronizationContext {

    /**
     * The coroutine is in synchronous context, and all tasks are scheduled on the main server thread
     */
    SYNC,
    /**
     * The coroutine is in asynchronous context, and all tasks are scheduled asynchronously to the main server thread.
     */
    ASYNC

}