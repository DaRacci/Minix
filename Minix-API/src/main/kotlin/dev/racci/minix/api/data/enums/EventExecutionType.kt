package dev.racci.minix.api.data.enums

/**
 * The mode how suspendable events are executed if dispatched manually.
 */
enum class EventExecutionType {
    Sequential,
    Concurrent
}
