package dev.racci.minix.api.collections

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apiguardian.api.API
import kotlin.time.Duration

@API(status = API.Status.MAINTAINED, since = "3.2.1")
class ExpiringArray<T>(val duration: Duration) : HashSet<T>() {

    /**
     * Attempts to add this element to the queue.
     * If the element is already part of the queue,
     * And the cooldown hasn't expired, the cooldown remains unchanged.
     *
     * @param element The element to add to the queue.
     * @return True, if not present or if the cooldown has expired, otherwise else.
     */
    override fun add(element: T): Boolean {
        if (contains(element)) return false

        val added = super.add(element)
        if (!added) return false

        runBlocking {
            delay(duration)
            remove(element)
        }

        return true
    }
}
