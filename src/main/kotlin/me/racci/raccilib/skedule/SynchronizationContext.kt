package me.racci.raccilib.skedule

/**
 * Represents a synchronization context that a BukkitScheduler coroutine is currently in.
 */
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