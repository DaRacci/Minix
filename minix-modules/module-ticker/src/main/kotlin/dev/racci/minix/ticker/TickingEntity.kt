package dev.racci.minix.ticker

import dev.racci.minix.api.data.Priority
import dev.racci.minix.api.extensions.reflection.getOrDefault
import kotlinx.datetime.Instant
import kotlin.properties.Delegates

public data class TickingEntity<E>(
    public val entity: E,
    public val priority: Priority = Priority.DEFAULT
) : Comparable<TickingEntity<*>> where E : Any {
    public var lastTick: Long by Delegates.notNull(); internal set
    public var isRunning: Boolean = false

    override fun toString(): String = buildString {
        append("TickingEntity(")
        append("entity=$entity, ")
        append("lastTick=${runCatching { lastTick }.getOrDefault(Instant.DISTANT_PAST)}, ")
        append("isRunning=$isRunning")
        append(")")
    }

    override fun compareTo(other: TickingEntity<*>): Int {
        return priority.compareTo(other.priority)
    }

    override fun hashCode(): Int {
        return entity.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TickingEntity<*>) return false

        return entity != other.entity
    }
}
