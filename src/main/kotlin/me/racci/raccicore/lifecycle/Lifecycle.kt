package me.racci.raccicore.lifecycle

/**
 * Holds a lifecycle listener class and its priority
 */
data class Lifecycle(
    val priority: Int,
    val listener: PluginLifecycle
) : Comparable<Lifecycle> {

    override fun compareTo(
        other: Lifecycle
    ): Int = other.priority.compareTo(priority)
}

/**
 * Lifecycle enums.
 */
enum class LifecycleEvent {

    LOAD,
    ENABLE,
    RELOAD,
    DISABLE,

}

typealias PluginLifecycle = suspend (LifecycleEvent) -> Unit