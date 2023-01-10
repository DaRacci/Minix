package dev.racci.minix.data.delegates

import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import org.jetbrains.annotations.TestOnly
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * A delegate that will monitor its access,
 * and if [ReadOnlyProperty.getValue] is called [count] within [delay], it will return a true value.
 *
 * @param delay The duration of time that if [count] calls are received within will result in a true result.
 * @param count The amount of access calls required to return a true state.
 */
@Experimental
@AvailableSince("5.0.0")
@OptIn(ExperimentalTime::class)
public class AccessWatcher private constructor(
    private val delay: Duration,
    private val count: Int,
    private val timeSource: TimeSource
) : ReadOnlyProperty<Any?, Boolean> {
    // TODO: Better way of initialising this?
    private var lastAccess: AtomicRef<TimeMark> = atomic(timeSource.markNow().minus(delay))
    private var accessCount: AtomicInt = atomic(0)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        if (lastAccess.value.elapsedNow() > delay) {
            lastAccess.lazySet(timeSource.markNow())
            accessCount.lazySet(0)
            return false
        }

        return accessCount.incrementAndGet() == count && reset()
    }

    private fun reset(): Boolean {
        lastAccess.lazySet(timeSource.markNow().minus(delay))
        accessCount.lazySet(0)
        return true
    }

    public companion object {
        /**
         * Creates a new AccessWatcher with the given delay and count.
         *
         * @param delay The duration of time that if [count] calls are received within will result in a true result.
         * @param count The amount of access calls required to return a true state.
         * @return A new AccessWatcher.
         */
        public fun of(
            delay: Duration,
            count: Int
        ): AccessWatcher = AccessWatcher(delay, count, TimeSource.Monotonic)

        @TestOnly
        internal fun of(
            delay: Duration,
            count: Int,
            timeSource: TimeSource
        ): AccessWatcher = AccessWatcher(delay, count, timeSource)
    }
}

/**
 * A Variant of [AccessWatcher] that will return true if the property is accessed twice within 500 Milliseconds.
 *
 * @param delay The duration of time that if [count] calls are received within will result in a true result.
 */
@Experimental
@AvailableSince("5.0.0")
@Suppress("UnusedReceiverParameter")
public fun Delegates.doubleAccessWatcher(
    delay: Duration = 500.milliseconds
): ReadOnlyProperty<Any?, Boolean> = AccessWatcher.of(delay, 2)
