package me.racci.raccilib.events

/**
 * Kotlin event
 *
 * @constructor
 *
 * @param async
 */
abstract class KotlinEvent(async: Boolean = false): org.bukkit.event.Event(async), org.bukkit.event.Cancellable {

    private var cancelled = false

    /**
     * Set cancelled
     *
     * @param cancel
     */
    override fun setCancelled(cancel: Boolean) {
        this.cancelled = true
    }

    /**
     * Is cancelled
     *
     * @return
     */
    override fun isCancelled(): Boolean {
        return cancelled
    }

    /**
     * Get handlers
     *
     * @return
     */
    override fun getHandlers(): org.bukkit.event.HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = org.bukkit.event.HandlerList()

        @JvmStatic
        fun getHandlerList(): org.bukkit.event.HandlerList {
            return handlerList
        }
    }

}