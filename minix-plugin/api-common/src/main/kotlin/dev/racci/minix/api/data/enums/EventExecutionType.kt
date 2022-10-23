package dev.racci.minix.api.data.enums

/**
 * The mode how suspendable events are executed if dispatched manually.
 */
public enum class EventExecutionType {
    Sequential,
    Concurrent
}
