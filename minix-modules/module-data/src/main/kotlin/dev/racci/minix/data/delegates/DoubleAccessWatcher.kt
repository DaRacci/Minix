package dev.racci.minix.data.delegates

import dev.racci.minix.api.utils.now
import kotlinx.datetime.Instant
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.milliseconds

public class DoubleAccessWatcher : ReadOnlyProperty<Any?, Boolean> {
    private var lastAccess: Instant? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        val current = now()

        lastAccess = when {
            lastAccess == null || current - lastAccess!! > DOUBLE_DELAY -> current
            else -> null
        }

        return lastAccess == null
    }

    private companion object {
        private val DOUBLE_DELAY = 500.milliseconds
    }
}
